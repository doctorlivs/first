package app;

import io.github.humbleui.jwm.*;

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
            // завершаем работу приложения
            App.terminate();
        }else if (event instanceof EventWindowCloseRequest) {
            window.close();
        }

    }
}

