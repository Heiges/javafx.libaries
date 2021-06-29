module biz.heiges.javafx {
    requires transitive javafx.controls;
	requires javafx.graphics;
    exports biz.heiges.javafx.libary.commons;
    exports biz.heiges.javafx.libary.tableview;
    exports biz.heiges.javafx.libary.tableview.cell;
    exports biz.heiges.javafx.libary.tableview.main;
}