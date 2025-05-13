module PrototypeJavaFX {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.sql;
	requires org.apache.poi.ooxml;
	requires itextpdf;
	requires java.desktop;
	requires javafx.swing;
	requires org.apache.poi.poi;
	
	opens application to javafx.graphics, javafx.fxml,javafx.base;

}
