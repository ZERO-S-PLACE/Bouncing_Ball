package org.zeros.bouncy_balls.Views;

import org.zeros.bouncy_balls.Controllers.MainWindowController;
import org.zeros.bouncy_balls.Controllers.P0_Login.LoginController;
import org.zeros.bouncy_balls.Controllers.P1_WelcomePanel.WelcomePanelController;
import org.zeros.bouncy_balls.Controllers.P2a_LevelTypeChoice.LevelTypeChoiceController;
import org.zeros.bouncy_balls.Controllers.P2b_Leaderboard.LeaderboardPanelController;
import org.zeros.bouncy_balls.Controllers.P2c_LevelCreator.LevelCreatorController;
import org.zeros.bouncy_balls.Controllers.P2d_Settings.SettingsPaneController;
import org.zeros.bouncy_balls.Controllers.P3_LevelSubtypeChoice.LevelSubtypeChoiceController;
import org.zeros.bouncy_balls.Controllers.P4_LevelSelection.LevelSelectionController;
import org.zeros.bouncy_balls.Controllers.P5_Animation.GameCountDownController;
import org.zeros.bouncy_balls.Controllers.P5_Animation.GamePausedController;

public class ControllersBase {


    private LoginController loginController;
    private WelcomePanelController welcomePanelController;
    private LevelTypeChoiceController levelTypeChoiceController;
    private LeaderboardPanelController leaderboardPanelController;
    private SettingsPaneController settingsPaneController;
    private LevelCreatorController levelCreatorController;
    private LevelSubtypeChoiceController levelSubtypeChoiceController;
    private LevelSelectionController levelSelectionController;
    private GameCountDownController gameCountDownController;
    private GamePausedController gamePausedController;
    private MainWindowController mainWindowController;

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

    public SettingsPaneController getSettingsController() {
        if (this.settingsPaneController == null) {
            this.settingsPaneController = new SettingsPaneController();
        }
        return settingsPaneController;
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

    public GameCountDownController getGameCountDownController() {
        if (this.gameCountDownController == null) {
            this.gameCountDownController = new GameCountDownController();
        }
        return gameCountDownController;
    }

    public GamePausedController getGamePausedController() {
        if (this.gamePausedController == null) {
            this.gamePausedController = new GamePausedController();
        }
        return gamePausedController;
    }


    public MainWindowController getMainWindowController() {
        if (this.mainWindowController == null) {
            this.mainWindowController = new MainWindowController();
        }
        return mainWindowController;
    }


}
