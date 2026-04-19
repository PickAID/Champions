package top.theillusivec4.champions.integrations.probejs;

import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.plugin.ProbeJSPlugin;
import top.theillusivec4.champions.integrations.probejs.docs.AffixEntityEffectDocs;
import top.theillusivec4.champions.integrations.probejs.docs.AffixLocationBasedEffectDocs;
import top.theillusivec4.champions.integrations.probejs.docs.AffixValueEffectDocs;
import top.theillusivec4.champions.integrations.probejs.docs.LevelBasedValueDocs;

public class ChampionsProbeJSPlugin extends ProbeJSPlugin {

  public static ChampionsProbeJSPlugin create() {
    return new ChampionsProbeJSPlugin();
  }

  @Override
  public void assignType(ScriptDump scriptDump) {
    LevelBasedValueDocs.assignType(scriptDump);
    AffixValueEffectDocs.assignType(scriptDump);
    AffixEntityEffectDocs.assignType(scriptDump);
    AffixLocationBasedEffectDocs.assignType(scriptDump);
  }
}
