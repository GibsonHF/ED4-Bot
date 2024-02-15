package net.botwithus;

import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.ImGuiWindowFlag;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.ScriptGraphicsContext;

public class SkeletonScriptGraphicsContext extends ScriptGraphicsContext {

    private MainScript script;

    public SkeletonScriptGraphicsContext(ScriptConsole scriptConsole, MainScript script) {
        super(scriptConsole);
        this.script = script;
    }

    @Override
    public void drawSettings() {
        if (ImGui.Begin("ED4 Bot", ImGuiWindowFlag.None.getValue())) {
            if (ImGui.BeginTabBar("My bar", ImGuiWindowFlag.None.getValue())) {
                if (ImGui.BeginTabItem("Settings", ImGuiWindowFlag.None.getValue())) {
                    script.runScript = ImGui.Checkbox("Run script", script.runScript);
                    if(!script.useMaxGuild && !script.usePontifexRing)
                    {
                        script.useWarsRetreat = ImGui.Checkbox("Use Wars Retreat", script.useWarsRetreat);
                        if (script.useWarsRetreat) {
                            script.useMaxGuild = false;
                            script.usePontifexRing = false;
                        }
                    }
                    if(!script.useWarsRetreat && !script.usePontifexRing)
                    {
                        script.useMaxGuild = ImGui.Checkbox("Use Max Guild", script.useMaxGuild);
                        if (script.useMaxGuild) {
                            script.useWarsRetreat = false;
                            script.usePontifexRing = false;
                        }
                    }
                    if(!script.useMaxGuild && !script.useWarsRetreat)
                    {
                        script.usePontifexRing = ImGui.Checkbox("Use Pontifex Ring", script.usePontifexRing);
                        if (script.usePontifexRing) {
                            script.useMaxGuild = false;
                            script.useWarsRetreat = false;
                        }
                    }
                    script.useDarkness = ImGui.Checkbox("Use Darkness", script.useDarkness);
                    script.useOverload = ImGui.Checkbox("Use Overload", script.useOverload);
                    script.usePrayerOrRestorePots = ImGui.Checkbox("Use Prayer Potions", script.usePrayerOrRestorePots);
                    script.useDeflectMagic = ImGui.Checkbox("Use Deflect Magic", script.useDeflectMagic);
                    ImGui.Text("My scripts state is: " + script.getBotState());
                    if(ImGui.Button("Set State past portal (DEBUG)"))
                    {
                        script.setBotState(MainScript.BotState.ATED4);
                    }
                    ImGui.EndTabItem();
                }
                if (ImGui.BeginTabItem("Statistics", ImGuiWindowFlag.None.getValue())) {

                    ImGui.Text("Total tokens: " + script.totalTokens);
                    ImGui.Text("Total runs: " + script.runCount);
                    ImGui.Text("Time running: " + timeRunningFormatted());
                    ImGui.EndTabItem();
                }
                ImGui.EndTabBar();
            }
            ImGui.End();
        }

    }

    private String timeRunningFormatted() {
        long timeRunning = System.currentTimeMillis() - script.runStartTime;
        long seconds = timeRunning / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return hours + "h " + minutes % 60 + "m " + seconds % 60 + "s";
    }

    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }
}
