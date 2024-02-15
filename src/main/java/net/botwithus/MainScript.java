package net.botwithus;

import net.botwithus.api.game.hud.Dialog;
import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Coordinate;
import net.botwithus.rs3.game.Travel;
import net.botwithus.rs3.game.actionbar.ActionBar;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;
import net.botwithus.rs3.util.RandomGenerator;

import static net.botwithus.rs3.game.Client.getLocalPlayer;

public class MainScript extends LoopingScript {

    public boolean runScript;
    private BotState botState = BotState.IDLE;

    enum BotState {
        WALKTOPORTAL, PRAYING, BANKING, ATED4, INSIDE_ZAMMY, ACCEPT_DIALOG, IDLE
    }

    public MainScript(String s, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(s, scriptConfig, scriptDefinition);
        this.sgc = new SkeletonScriptGraphicsContext(getConsole(), this);
        botState = BotState.IDLE;
    }

    @Override
    public void onLoop() {
        if(!runScript) {
            return;
        }
        LocalPlayer player = Client.getLocalPlayer();
        switch (botState) {
            case IDLE -> {
                useWarsTeleport();
            }
            case BANKING -> {
                LoadPresetLogic();
            }
            case PRAYING -> {
                useAltarofWar();
            }
            case WALKTOPORTAL -> {
                walkToPortal();
            }
            case ATED4 -> {
                enterZammy();
            }
            case ACCEPT_DIALOG -> {
                acceptDialog();
            }
            case INSIDE_ZAMMY -> {
                attackMiniBoss();
            }


        }
    }

    private void walkToPortal() {
        if (getLocalPlayer() != null) {
            if (!getLocalPlayer().isMoving()) {
                EntityResultSet<SceneObject> sceneObjectQuery = SceneObjectQuery.newQuery().name("Portal (The Zamorakian Undercity)").results();
                if(!sceneObjectQuery.isEmpty())
                {
                    SceneObject portal = sceneObjectQuery.nearest();
                    portal.interact("Enter");
                    botState = BotState.ATED4;
                }
            }
        }
    }

    public void enterZammy(){
        if(getLocalPlayer() != null)
        {
            if (!getLocalPlayer().isMoving()) {
                EntityResultSet<SceneObject> sceneObjectQuery = SceneObjectQuery.newQuery().name("The Zamorakian Undercity").results();
                if(!sceneObjectQuery.isEmpty())
                {
                    SceneObject portal = sceneObjectQuery.nearest();
                    portal.interact("Enter");
                    botState = BotState.ACCEPT_DIALOG;
                }
            }
        }
    }

    private void useAltarofWar() {
        if(WalkTo(3303, 10127)) {
            EntityResultSet<SceneObject> query = SceneObjectQuery.newQuery().name("Altar of War").results();
            if (!query.isEmpty()) {
                SceneObject altar = query.nearest();
                altar.interact("Pray");
                botState = BotState.WALKTOPORTAL;
            }
        }
    }

    private void LoadPresetLogic() {
        if(WalkTo(3299, 10131))
        {
            EntityResultSet<SceneObject> bankChest = SceneObjectQuery.newQuery().name("Bank chest").results();
            if(!bankChest.isEmpty())
            {
                println("Bank chest found!");
                SceneObject bank = bankChest.nearest();
                bank.interact("Load Last Preset from");
                //wait at bank if we aren't full health
                if(getLocalPlayer().getCurrentHealth() < getLocalPlayer().getMaximumHealth())
                {
                    println("Healing up!");
                    Execution.delay(RandomGenerator.nextInt(600,1000));
                }else {
                    botState = BotState.PRAYING;
                }
            }
        }
    }

    public void usePontifex()
    {
        //insert logic here
    }

    public void attackMiniBoss()
    {
        if(getLocalPlayer() != null) {
            if (!getLocalPlayer().isMoving()) {

            }
        }

    }

    private void acceptDialog() {
            if (Dialog.isOpen()) {

                    Dialog.getOptions().forEach(option -> {
                        println(option);
                        if (option == null)
                            return;


                        if(option.contains("No"))
                        {
                            Execution.delay(RandomGenerator.nextInt(200, 300));
                            Dialog.interact(option);
                            println("Accepting first dialog");
                        }



                        if (option.contains("Normal mode")) {
                            Execution.delay(RandomGenerator.nextInt(500, 550));
                            Dialog.interact(option);
                            println("Accepting dialog!");
                            botState = BotState.INSIDE_ZAMMY;
                        }
                    });
                }

    }

    private void useWarsTeleport() {
        if (getLocalPlayer() != null) {
            ActionBar.useAbility("War's Retreat Teleport");
            botState = BotState.BANKING;
        }
    }


    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }


    public boolean WalkTo(int x, int y) {
        if (getLocalPlayer() != null) {
            Coordinate myPos = getLocalPlayer().getCoordinate();
            if(myPos.getX() != x && myPos.getY() != y) {

                if (!getLocalPlayer().isMoving()) {
                    println("Walking to: " + x + ", " + y);
                    Travel.walkTo(x, y);
                    Execution.delay(RandomGenerator.nextInt(300, 500));
                }
                return false;
            }else {
                return true;
            }
        }
        return false;
    }
}
