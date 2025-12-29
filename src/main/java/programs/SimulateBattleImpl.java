package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        // Ваше решение
        int round = 1;

        while (true) {


            List<Unit> alivePlayerUnits = new ArrayList<>();
            List<Unit> aliveComputerUnits = new ArrayList<>();

            for (Unit unit : playerArmy.getUnits()) {
                if (unit.isAlive()) {
                    alivePlayerUnits.add(unit);
                }
            }

            for (Unit unit : computerArmy.getUnits()) {
                if (unit.isAlive()) {
                    aliveComputerUnits.add(unit);
                }
            }


            if (alivePlayerUnits.isEmpty() || aliveComputerUnits.isEmpty()) {
                break;
            }


            List<Unit> allUnits = new ArrayList<>();
            allUnits.addAll(alivePlayerUnits);
            allUnits.addAll(aliveComputerUnits);


            allUnits.sort((u1, u2) -> Integer.compare(
                    u2.getBaseAttack(),
                    u1.getBaseAttack()
            ));
            List<Unit> killedThisRound = new ArrayList<>();
            for (Unit attacker : allUnits) {
                if (!attacker.isAlive() || killedThisRound.contains(attacker)) {
                    continue;
                }

                Unit target = attacker.getProgram().attack();

                if (target != null && (target.isAlive() || !killedThisRound.contains(target))) {

                    printBattleLog(attacker, target);

                    performAttack(attacker, target);

                    if (target.getHealth() <= 0) {
                        target.setHealth(0);
                        target.setAlive(false);
                        killedThisRound.add(target);
                    }
                }
            }

            round++;
        }
    }

    private void performAttack(Unit attacker, Unit target) {

        int baseDamage = attacker.getBaseAttack();
        double multiplier = 1.0;


        Map<String, Double> attackBonuses = attacker.getAttackBonuses();
        if (attackBonuses != null && attackBonuses.containsKey(target.getUnitType())) {
            multiplier *= attackBonuses.get(target.getUnitType());
        }


        Map<String, Double> defenceBonuses = target.getDefenceBonuses();
        if (defenceBonuses != null && defenceBonuses.containsKey(attacker.getAttackType())) {
            multiplier /= defenceBonuses.get(attacker.getAttackType());
        }

        int finalDamage = (int) Math.round(baseDamage * multiplier);


        target.setHealth(target.getHealth() - finalDamage);

    }

    private void printBattleLog(Unit attacker, Unit target) {
        printBattleLog.printBattleLog(attacker, target);
    }
}