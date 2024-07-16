module org.zeros.bouncy_balls {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;
    requires de.jensd.fx.glyphs.fontawesome;

    exports org.zeros.bouncy_balls.Applications.GameApplication.Model;
    exports org.zeros.bouncy_balls.Calculations;
    exports org.zeros.bouncy_balls.Calculations.Equations;
    exports org.zeros.bouncy_balls.Applications.GameApplication.Controllers;
    exports org.zeros.bouncy_balls.Applications.GameApplication.Views;
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
    exports org.zeros.bouncy_balls.Applications.GameApplication.Controllers.P0_Login;
    exports org.zeros.bouncy_balls.Applications.GameApplication.Controllers.P1_WelcomePanel;
    exports org.zeros.bouncy_balls.Applications.GameApplication.Controllers.P2a_LevelTypeChoice;
    exports org.zeros.bouncy_balls.Applications.GameApplication.Controllers.P2b_Leaderboard;
    exports org.zeros.bouncy_balls.Applications.GameApplication.Controllers.P2d_Settings;
    exports org.zeros.bouncy_balls.Applications.GameApplication.Controllers.P3_LevelSubtypeChoice;
    exports org.zeros.bouncy_balls.Applications.GameApplication.Controllers.P4_LevelSelection;
    exports org.zeros.bouncy_balls.Applications.GameApplication.Controllers.P5_Animation;
    exports org.zeros.bouncy_balls.DisplayUtil;
    exports org.zeros.bouncy_balls.Applications.CreatorApplication;
    exports org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers;
    exports org.zeros.bouncy_balls.Applications.CreatorApplication.Views;
    exports org.zeros.bouncy_balls.Applications.CreatorApplication.Models;
    exports org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LeftPanel;
    exports org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.CreatorSettings;
    exports org.zeros.bouncy_balls.Applications.CreatorApplication.TrackingPane;
    exports org.zeros.bouncy_balls.Level.Enums;
    exports org.zeros.bouncy_balls.Applications.GameApplication;

    opens org.zeros.bouncy_balls;


}