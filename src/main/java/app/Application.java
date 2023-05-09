package app;

import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.*;
import io.github.humbleui.skija.*;
import misc.Misc;

import java.io.File;
import java.util.function.Consumer;

import static app.Colors.APP_BACKGROUND_COLOR;

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
            paint(s.getCanvas(), s.getWidth(), s.getHeight());
        }

    }

    /**
     * Рисование
     *
     * @param canvas низкоуровневый инструмент рисования примитивов от Skija
     * @param height высота окна
     * @param width  ширина окна
     */
    public void paint(Canvas canvas, int width, int height) {
        // запоминаем изменения (пока что там просто заливка цветом)
        canvas.save();
        // очищаем канвас
        canvas.clear(APP_BACKGROUND_COLOR);

        // координаты левого верхнего края окна
        int rX = width / 3;
        int rY = height / 3;
        // ширина и высота
        int rWidth = width / 3;
        int rHeight = height / 3;
        // создаём кисть
        Paint paint = new Paint();
        // задаём цвет рисования
        paint.setColor(Misc.getColor(100, 255, 255, 255));
        // рисуем квадрат
        canvas.drawRRect(RRect.makeXYWH(rX, rY, rWidth, rHeight, 4), paint);

        // восстанавливаем состояние канваса
        canvas.restore();
    }
}

