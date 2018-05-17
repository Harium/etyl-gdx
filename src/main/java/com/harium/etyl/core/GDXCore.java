package com.harium.etyl.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.harium.etyl.commons.context.Context;
import com.harium.etyl.commons.context.Session;
import com.harium.etyl.commons.context.UpdateIntervalListener;
import com.harium.etyl.commons.context.load.*;
import com.harium.etyl.commons.event.*;
import com.harium.etyl.commons.module.ModuleHandler;
import com.harium.etyl.core.animation.Animation;
import com.harium.etyl.core.graphics.GDXGraphics;
import com.harium.etyl.core.graphics.Graphics;
import com.harium.etyl.core.input.keyboard.Keyboard;
import com.harium.etyl.loader.Assets;
import com.harium.etyl.loader.FontLoader;
import com.harium.etyl.loader.Loader;
import com.harium.etyl.loader.MultimediaLoader;
import com.harium.etyl.loader.image.ImageLoader;

public class GDXCore<T extends Context> extends ApplicationAdapter implements InputProcessor, LoaderListener<Context> {
    //GDXCore
    protected Session session = new Session();

    protected GDXGraphics graphics;

    protected Camera orthoCamera;

    protected Context application;
    protected DefaultLoadApplication loadContext;
    protected Context context;

    // Mouse stuff
    private boolean dragged;
    private int dragX, dragY;
    private PointerEvent event = new PointerEvent();

    // Keyboard Stuff
    private KeyEvent keyDown = new KeyEvent(0, KeyState.PRESSED);
    private KeyEvent keyUp = new KeyEvent(0, KeyState.RELEASED);
    private KeyEvent keyTyped = new KeyEvent(0, KeyState.TYPED);

    private static final Vector2 tmpVec = new Vector2();

    protected Viewport viewport;

    protected int w = 1024;
    protected int h = 576;

    protected Assets assetManager;

    protected boolean loading = false;
    protected boolean changed = false;
    protected boolean initContext = true;
    private boolean created = false;

    // Avoid bugs in Zenfone 3
    // That always fires touchDragged after touchDown
    private long lastTouchDown = 0;
    private static final long DRAG_INTERVAL = 100;

    protected ApplicationLoader applicationLoader;
    private ModuleHandler modules = new ModuleHandler();

    public GDXCore(int w, int h) {
        super();

        this.w = w;
        this.h = h;

        assetManager = new Assets();
        applicationLoader = new ApplicationLoader(w, h);

        // Setup Loaders
        ImageLoader.getInstance().setAssets(assetManager);
        MultimediaLoader.getInstance().setAssets(assetManager);
        FontLoader.getInstance().setAssets(assetManager);
    }

    @Override
    public void create() {
        orthoCamera = new OrthographicCamera();
        viewport = new FitViewport(w, h, orthoCamera);
        viewport.apply();

        orthoCamera.position.set(orthoCamera.viewportWidth / 2, orthoCamera.viewportHeight / 2, 0);

        init();
    }

    @Override
    public void dispose() {
        FontLoader.getInstance().dispose();
        assetManager.clear();
    }

    protected void init() {
        graphics = new GDXGraphics(w, h);
        graphics.setOrthographicCamera(orthoCamera);
        graphics.setProjectionMatrix(orthoCamera.combined);

        // Override Back Button Behavior
        // Without this, dispose and reload does not work well
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(this);

        initModules();
    }

    protected void initModules() {
        modules.add(Animation.getInstance());
        //modules.add(AdHandler.getInstance());
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!created) {
            return true;
        }
        keyDown.setKey(Keyboard.getAwtKeyCode(keycode));
        keyDown.setConsumed(false);
        context.updateKeyboard(keyDown);
        return keyDown.isConsumed();
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!created) {
            return true;
        }
        keyUp.setKey(Keyboard.getAwtKeyCode(keycode));
        keyUp.setConsumed(false);
        context.updateKeyboard(keyUp);
        return keyUp.isConsumed();
    }

    @Override
    public boolean keyTyped(char character) {
        if (!created) {
            return true;
        }
        keyTyped.setChar(character);
        keyTyped.setConsumed(false);
        context.updateKeyboard(keyTyped);
        return keyTyped.isConsumed();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!created) {
            return true;
        }
        Vector2 vector = worldVector(screenX, screenY);
        int px = (int) vector.x;
        int py = (int) (h - vector.y);

        event.set(mouseKey(button), PointerState.PRESSED, px, py);
        event.setPointer(pointer);
        event.resetTimestamp();
        lastTouchDown = event.getTimestamp();

        handleMouse();

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!created) {
            return true;
        }

        Vector2 vector = worldVector(screenX, screenY);
        int px = (int) vector.x;
        int py = (int) (h - vector.y);

        event.set(mouseKey(button), PointerState.RELEASED, px, py);
        event.setPointer(pointer);
        event.resetTimestamp();

        handleMouse();

        dragged = false;

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!created) {
            return true;
        }
        event.resetTimestamp();
        if (event.getTimestamp() - lastTouchDown < DRAG_INTERVAL) {
            return true;
        }

        Vector2 vector = worldVector(screenX, screenY);
        int px = (int) vector.x;
        int py = (int) (h - vector.y);

        if (!dragged) {
            dragX = px;
            dragY = py;

            dragged = true;
        }

        int deltaX = px - dragX;
        int deltaY = py - dragY;

        event.set(MouseEvent.MOUSE_BUTTON_LEFT, PointerState.DRAGGED, px, py, deltaX, deltaY);
        event.setPointer(pointer);

        handleMouse();
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (!created) {
            return true;
        }
        Vector2 vector = worldVector(screenX, screenY);
        int px = (int) vector.x;
        int py = (int) (h - vector.y);

        event.set(MouseEvent.MOUSE_NONE, PointerState.MOVE, px, py);
        event.resetTimestamp();
        lastTouchDown = event.getTimestamp();

        handleMouse();
        return false;
    }

    private void handleMouse() {
        context.updateMouse(event);
        modules.updateMouse(event);
    }

    private MouseEvent mouseKey(int button) {
        MouseEvent key = MouseEvent.MOUSE_NONE;
        if (button == Input.Buttons.LEFT) {
            key = MouseEvent.MOUSE_BUTTON_LEFT;
        } else if (button == Input.Buttons.RIGHT) {
            key = MouseEvent.MOUSE_BUTTON_RIGHT;
        } else if (button == Input.Buttons.MIDDLE) {
            key = MouseEvent.MOUSE_BUTTON_MIDDLE;
        }
        return key;
    }

    private Vector2 worldVector(int screenX, int screenY) {
        viewport.unproject(tmpVec.set(screenX, screenY));
        return tmpVec;
    }

    @Override
    public boolean scrolled(int amount) {
        if (!created) {
            return true;
        }
        return false;
    }

    public boolean updateApplication(Context context, long now) {
        if (context.getUpdateInterval() == 0) {

            context.update(now);
            context.setLastUpdate(now);

        } else if (now - context.getLastUpdate() >= context.getUpdateInterval()) {

            UpdateIntervalListener updated = context.getUpdated();

            if (updated == null) {
                return false;
            }

            updated.timeUpdate(now);
            context.setLastUpdate(now);
        }

        return true;
    }

    public Camera getCamera() {
        return orthoCamera;
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        orthoCamera.position.set(orthoCamera.viewportWidth / 2, orthoCamera.viewportHeight / 2, 0);

        graphics.setProjectionMatrix(orthoCamera.combined);
    }

    @Override
    public void render() {
        createFirstApplication();
        long now = System.currentTimeMillis();
        update(now);

        updateLoading(loadContext);

        renderContext(context);
        drawModules(graphics);

        //spriteBatch.end() if needed
        graphics.flush();
    }

    protected void createFirstApplication() {
        if (!created) {
            // Create first Application
            reloadApplication(application);
            created = true;
        }
    }

    protected void updateLoading(LoadApplication loadContext) {
        if (loading && !assetManager.update()) {
            float loading = 100 * assetManager.getProgress();
            loadContext.onChangeLoad(loading);
            assetManager.checkResources();
        } else if (!changed) {
            assetManager.checkResources();
            context = application;
            context.initGraphics(graphics);
            initContext = true;
            loading = false;
            changed = true;
        } else {
            // When Application is already loaded, but it want to load something on demand
            if (!assetManager.update()) {
                context.setLoaded(false);
                assetManager.checkResources();
            } else if (!context.isLoaded()) {
                context.setLoaded(true);
                assetManager.checkResources();
            }
        }
    }

    private void setup2D() {
        orthoCamera.update();
        graphics.setProjectionMatrix(orthoCamera.combined);
        // Not needed
        //viewport.apply();
    }

    protected void renderContext(Context context) {
        setup2D();

        if (context.isClearBeforeDraw()) {
            //Use Black as clear color
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        context.draw(graphics);
    }

    protected void drawModules(Graphics graphics) {
        modules.draw(graphics);
    }

    protected void update(long now) {
        updateApplication(context, now);
        modules.update(now);

        if (context.getNextApplication() != context) {
            reloadApplication(context.getNextApplication());
        }
    }

    public void reloadApplication(Context nextApplication) {
        loading = true;
        changed = false;

        //Dispose Modules
        modules.dispose(application);
        if (application.isLoaded()) {
            application.dispose();
        }

        application = nextApplication;
        application.setSession(session);

        buildLoadContext();

        //Init Modules
        modules.init(application);
        //Load Async
        loadApplication();
    }

    protected void buildLoadContext() {
        if (application.getLoadApplication() == null) {
            loadContext = new GenericLoadApplication(application.getW(), application.getH());
        } else {
            loadContext = application.getLoadApplication();
        }

        //Force load the loading screen
        loadContext.setSession(session);
        loadContext.load();

        assetManager.finishLoading();
        assetManager.checkResources();

        context = loadContext;
    }

    protected void loadApplication() {
        application.load();
    }

    public Session getSession() {
        return session;
    }

    public void setApplication(Context application) {
        this.application = application;
    }

    @Override
    public void onLoad(Context context) {
        setApplication(context);
        context.initGraphics(graphics);
        context.setLoaded(true);

        this.context = context;
    }

    public void addLoader(Loader loader) {

    }

    public Assets getAssets() {
        return assetManager;
    }
}
