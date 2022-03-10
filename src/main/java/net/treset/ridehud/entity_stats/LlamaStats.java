package net.treset.ridehud.entity_stats;

import net.minecraft.entity.passive.LlamaEntity;

public class LlamaStats extends VehicleStats {

    public LlamaEntity llama = null;

    public LlamaStats(LlamaEntity llama) {
        this.uuid = llama.getUuidAsString();
        this.entity = llama;
        this.llama = llama;

        this.healthMin = 15;
        this.healthMax = 30;
        this.health = (int)llama.getMaxHealth();
        this.healthHearts = this.health / 2;
        this.healthScore = (int)((float)(this.health - this.healthMin) / (float)(healthMax - this.healthMin) * 100);

        this.healthCurrent = (int)llama.getHealth();

        this.score = (this.healthScore);
    }

    public void updateCurrentHealth() {
        this.healthCurrent = (int)this.llama.getHealth();
    }
}
