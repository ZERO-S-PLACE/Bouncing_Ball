module org.zeros.bouncy_balls {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires commons.math3;

    opens org.zeros.bouncy_balls.App to javafx.fxml;

    exports org.zeros.bouncy_balls.Model;
    exports org.zeros.bouncy_balls.Calculations;
    exports org.zeros.bouncy_balls.Calculations.Equations;
    exports org.zeros.bouncy_balls.Controllers;
    exports org.zeros.bouncy_balls.Views;
    exports org.zeros.bouncy_balls.Objects.MovingObjects;
    exports org.zeros.bouncy_balls.Objects.VectorArea;
    exports org.zeros.bouncy_balls.Level;
    exports org.zeros.bouncy_balls.Animation.InputOnRun;
    exports org.zeros.bouncy_balls.Animation.Borders;
    exports org.zeros.bouncy_balls.Animation.Animation;

    exports org.zeros.bouncy_balls.Calculations.AreasMath;
    exports org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment;
    exports org.zeros.bouncy_balls.Calculations.ConvexHull;
    exports org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea;
    exports org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea;
    exports org.zeros.bouncy_balls.Controllers.P0_Login;
    exports org.zeros.bouncy_balls.Controllers.P1_WelcomePanel;
    exports org.zeros.bouncy_balls.Controllers.P2a_LevelTypeChoice;
    exports org.zeros.bouncy_balls.Controllers.P2b_Leaderboard;
    exports org.zeros.bouncy_balls.Controllers.P2c_LevelCreator;
    exports org.zeros.bouncy_balls.Controllers.P2d_Settings;
    exports org.zeros.bouncy_balls.Controllers.P3_LevelSubtypeChoice;
    exports org.zeros.bouncy_balls.Controllers.P4_LevelSelection;
    exports org.zeros.bouncy_balls.Controllers.P5_Animation;

    exports org.zeros.bouncy_balls.App;
    exports org.zeros.bouncy_balls.DisplayUtil;


}