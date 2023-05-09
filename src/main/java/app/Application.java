package app;

import io.github.humbleui.jwm.*;

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
            window.setVisible(true);
        }

    /**
     * Обработчик событий
     * @param event событие
     */
    @Override
    public void accept(Event event) {
        if (event instanceof EventWindowClose) {
            // завершаем работу приложения
            App.terminate();
        }else if (event instanceof EventWindowCloseRequest) {
            window.close();
        }

    }
}

