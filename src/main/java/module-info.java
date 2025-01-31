module labzeroseven {
    requires static lombok;
    requires org.slf4j;
    requires javafx.controls;
    requires javafx.graphics;
    requires sewagelib;
    exports pl.pwr.ite.dynak.gui;
    exports pl.pwr.ite.dynak.services;
    exports pl.pwr.ite.dynak.utils;
}