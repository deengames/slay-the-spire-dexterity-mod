package com.deengames.slaythespire.dexteritymod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostDeathSubscriber;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SpireInitializer
/// Stores the number of games played in a text file; uses that to determine amount of upgrades.
public class DexterityOnDeathMod implements ISubscriber, OnStartBattleSubscriber, PostDeathSubscriber {

	private final String DATA_FILE_NAME = "DexterityMod.dat";
	private final int DEXTERITY_PER_GAME = 1;

	public static void initialize() {
        new DexterityOnDeathMod();
    }
	
	public DexterityOnDeathMod() {
		BaseMod.subscribe(this);
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom battleRoom) {
		AbstractPlayer player = AbstractDungeon.player;
		int numGames = getNumGames();
		AbstractPower dexterityPower = new DexterityPower(player, numGames * DEXTERITY_PER_GAME);
		player.powers.add(dexterityPower);
	}

	@Override
	public void receivePostDeath() {
		int numGames = getNumGames();
		numGames += 1;

		setNumGames(numGames);
	}

	private int getNumGames() {
		if (!Files.exists(Paths.get(DATA_FILE_NAME)))
		{
			return 0;
		}

		try {
			List<String> lines = Files.readAllLines(Paths.get(DATA_FILE_NAME));
			final String contents = String.join("\n", lines);
			final int numGames = Integer.parseInt(contents);
			return numGames;
		} catch (IOException e)
		{
			return 0;
		}
	}

	private void setNumGames(int numGames) {
		try {
			final String contents = String.valueOf(numGames);
			final byte[] asByte = contents.getBytes();
			Files.write(Paths.get(DATA_FILE_NAME), asByte);
		} catch (IOException e)
		{

		}
	}
}
