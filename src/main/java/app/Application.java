package app;

import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.EventFrameSkija;
import io.github.humbleui.skija.Surface;

import java.io.File;
import java.util.function.Consumer;
/**
 * Класс окна приложения
 */

public class Application implements Consumer<Event> {
    /**
     *
     */
        private final Window window;
    /**
     * Конструктор окна приложения
     */
        public Application() {
            window = App.makeWindow();
            window.setEventListener(this);
            // названия слоёв, которые будем перебирать
            String[] layerNames = new String[]{
                    "LayerGLSkija", "LayerRasterSkija"
            };

            // перебираем слои
            for (String layerName : layerNames) {
                String className = "io.github.humbleui.jwm.skija." + layerName;
                try {
                    Layer layer = (Layer) Class.forName(className).getDeclaredConstructor().newInstance();
                    window.setLayer(layer);
                    break;
                } catch (Exception e) {
                    System.out.println("Ошибка создания слоя " + className);
                }
            }

            // если окну не присвоен ни один из слоёв
            if (window._layer == null)
                throw new RuntimeException("Нет доступных слоёв для создания");
            // делаем окно видимым
            window.setVisible(true);


            switch (Platform.CURRENT) {
                case WINDOWS -> window.setIcon(new File("src/main/resources/windows.ico"));
                case MACOS -> window.setIcon(new File("src/main/resources/macos.icns"));
            }

            window.setWindowSize(900, 900);
            window.setWindowPosition(100, 100);


            window.setVisible(true);
            window.setTitle("Java 2D");
        }

    /**
     * Обработчик событий
     * @param event событие
     */
    @Override
    public void accept(Event event) {
        if (event instanceof EventWindowClose) {
            App.terminate();
        }else if (event instanceof EventWindowCloseRequest) {
            window.close();
        } else if (event instanceof EventFrameSkija ee) {
            Surface s = ee.getSurface();
            s.getCanvas().clear(Colors.APP_BACKGROUND_COLOR);
        }

    }
}

