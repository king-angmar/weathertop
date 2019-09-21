package xyz.wongs.weather;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import xyz.wongs.weather.design.observer.ObserverImpl;
import xyz.wongs.weather.design.observer.Subject;


public class AppTest {

    @Test
    public void testObserver() {

        Subject targetSubject = new Subject();

        ObserverImpl observerDesign1 = new ObserverImpl();

        ObserverImpl observerDesign2 = new ObserverImpl();

        ObserverImpl observerDesign3 = new ObserverImpl();
        ObserverImpl observerDesign4 = new ObserverImpl();

        targetSubject.addObserver(observerDesign1);
        targetSubject.addObserver(observerDesign2);
        targetSubject.addObserver(observerDesign3);
        targetSubject.addObserver(observerDesign4);

        targetSubject.setMsg(Thread.currentThread().getName());

        System.out.println(observerDesign1.getMsg());
        System.out.println(observerDesign2.getMsg());
        System.out.println(observerDesign3.getMsg());
        System.out.println(observerDesign4.getMsg());
    }
}
