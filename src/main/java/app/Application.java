package app;

import controls.Label;
import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.*;
import io.github.humbleui.skija.*;
import misc.CoordinateSystem2i;
import misc.Misc;

import java.io.File;
import java.util.function.Consumer;

import static app.Colors.APP_BACKGROUND_COLOR;
import static app.Colors.PANEL_BACKGROUND_COLOR;

/**
 * Класс окна приложения
 */

public class Application implements Consumer<Event> {
    /**
     *
     */
        private Window window = null;
    /**
     * Конструктор окна приложения
     */
        public Application() {
            label = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING, "Привет, мир!");
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
     * Первый заголовок
     */
    private final Label label;
    /**
     * радиус скругления элементов
     */
    public static final int C_RAD_IN_PX = 4;
    /**
     * отступы панелей
     */
    public static final int PANEL_PADDING = 5;

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
            paint(s.getCanvas(), new CoordinateSystem2i(s.getWidth(), s.getHeight()));
        }

    }

    /**
     * Рисование
     *
     * @param canvas   низкоуровневый инструмент рисования примитивов от Skija
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // запоминаем изменения (пока что там просто заливка цветом)
        canvas.save();
        // очищаем канвас
        canvas.clear(APP_BACKGROUND_COLOR);
        // рисуем заголовок
        label.paint(canvas, windowCS);
        // восстанавливаем состояние канваса
        canvas.restore();
        // рисуем заголовок в точке [100,100] с шириной и высотой 200
        label.paint(canvas, new CoordinateSystem2i(100, 100, 200, 200));
    }
}

