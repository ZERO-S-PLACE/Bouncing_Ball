module org.zeros.bouncy_balls {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires commons.math3;

    opens org.zeros.bouncy_balls to javafx.fxml;

    exports org.zeros.bouncy_balls;
    exports org.zeros.bouncy_balls.Model;
    exports org.zeros.bouncy_balls.Calculations;
    exports org.zeros.bouncy_balls.Calculations.Equations;
    exports org.zeros.bouncy_balls.Controllers;
    exports org.zeros.bouncy_balls.Views;
    exports org.zeros.bouncy_balls.Objects.MovingObjects;
    exports org.zeros.bouncy_balls.Objects.Area;
    exports org.zeros.bouncy_balls.Level;
    exports org.zeros.bouncy_balls.Animation.InputOnRun;
    exports org.zeros.bouncy_balls.Animation.Borders;
    exports org.zeros.bouncy_balls.Animation.Animation;

    opens org.zeros.bouncy_balls.Level to javafx.fxml;
    exports org.zeros.bouncy_balls.Calculations.AreasMath;
    exports org.zeros.bouncy_balls.Objects.Area.PolyLineSegment;
    exports org.zeros.bouncy_balls.Exceptions;

}