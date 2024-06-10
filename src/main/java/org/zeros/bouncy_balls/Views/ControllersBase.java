package org.zeros.bouncy_balls.Views;

import org.zeros.bouncy_balls.Controllers.P0_Login.LoginController;
import org.zeros.bouncy_balls.Controllers.P1_WelcomePanel.WelcomePanelController;
import org.zeros.bouncy_balls.Controllers.P2a_LevelTypeChoice.LevelTypeChoiceController;
import org.zeros.bouncy_balls.Controllers.P2b_Leaderboard.LeaderboardPanelController;
import org.zeros.bouncy_balls.Controllers.P2c_LevelCreator.LevelCreatorController;
import org.zeros.bouncy_balls.Controllers.P3_LevelSubtypeChoice.LevelSubtypeChoiceController;
import org.zeros.bouncy_balls.Controllers.P4_LevelSelection.LevelSelectionController;
import org.zeros.bouncy_balls.Controllers.P5_Animation.GameEndController;
import org.zeros.bouncy_balls.Controllers.P5_Animation.GamePanelController;
import org.zeros.bouncy_balls.Controllers.P5_Animation.GamePausedController;

public class ControllersBase {


    private LoginController loginController;
    private WelcomePanelController welcomePanelController;
    private LevelTypeChoiceController levelTypeChoiceController;
    private LeaderboardPanelController leaderboardPanelController;
    private LevelCreatorController levelCreatorController;
    private LevelSubtypeChoiceController levelSubtypeChoiceController;
    private LevelSelectionController levelSelectionController;
    private GameEndController gameEndController;
    private GamePanelController gamePanelController;
    private GamePausedController gamePausedController;



    public ControllersBase (){}
    public LoginController getLoginController() {
        if (this.loginController == null) {
            this.loginController = new LoginController();
        }
        return loginController;
    }


    public WelcomePanelController getWelcomePanelController() {
        if (this.welcomePanelController == null) {
            this.welcomePanelController = new WelcomePanelController();
        }
        return welcomePanelController;
    }
    public LevelTypeChoiceController getLevelTypeChoiceController() {
        if (this.levelTypeChoiceController == null) {
            this.levelTypeChoiceController = new LevelTypeChoiceController();
        }
        return levelTypeChoiceController;
    }
    public LeaderboardPanelController getLeaderboardPanelController() {
        if (this.leaderboardPanelController == null) {
            this.leaderboardPanelController = new LeaderboardPanelController();
        }
        return leaderboardPanelController;
    }
    public LevelCreatorController getLevelCreatorController() {
        if (this.levelCreatorController == null) {
            this.levelCreatorController = new LevelCreatorController();
        }
        return levelCreatorController;
    }
    public LevelSubtypeChoiceController getLevelSubtypeChoiceController() {
        if (this.levelSubtypeChoiceController == null) {
            this.levelSubtypeChoiceController = new LevelSubtypeChoiceController();
        }
        return levelSubtypeChoiceController;
    }
    public LevelSelectionController getLevelSelectionController() {
        if (this.levelSelectionController == null) {
            this.levelSelectionController = new LevelSelectionController();
        }
        return levelSelectionController;
    }
    public GameEndController getGameEndController() {
        if (this.gameEndController == null) {
            this.gameEndController = new GameEndController();
        }
        return gameEndController;
    }
    public GamePanelController getGamePanelController() {
        if (this.gamePanelController == null) {
            this.gamePanelController = new GamePanelController();
        }
        return gamePanelController;
    }
    public GamePausedController getGamePausedController() {
        if (this.gamePausedController == null) {
            this.gamePausedController = new GamePausedController();
        }
        return gamePausedController;
    }





}
