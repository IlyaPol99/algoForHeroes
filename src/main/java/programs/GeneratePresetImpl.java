package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int ARMY_WIDTH = 3;
    private static final int ARMY_HEIGHT = 21;
    private static final int MAX_UNITS_PER_TYPE = 11;
    Random random = new Random();

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // Ограничение по количеству юнитов
        int maxUnitsPerType = 11;

        unitList.sort((u1, u2) -> {
            double attackRatio1 = (double) u1.getBaseAttack() / u1.getCost();
            double attackRatio2 = (double) u2.getBaseAttack() / u2.getCost();
            if (attackRatio1 != attackRatio2) {
                return Double.compare(attackRatio2, attackRatio1);
            }
            double healthRatio1 = (double) u1.getHealth() / u1.getCost();
            double healthRatio2 = (double) u2.getHealth() / u2.getCost();
            return Double.compare(healthRatio2, healthRatio1);
        });

        Army army = new Army();
        int remainingPoints = maxPoints;
        int unitCounter = 0;
        Set<Coordinates> coordinatesList = new HashSet<>();
        List<Unit> unitsInArmy = new ArrayList<>();
        for (Unit unit : unitList) {

            int maxUnits = Math.min(MAX_UNITS_PER_TYPE, remainingPoints / unit.getCost());

            for (int i = 0; i < maxUnits; i++) {

                String unitName = unit.getName() + " " + (unitCounter + 1);
                Coordinates coordinates = new Coordinates(random.nextInt(ARMY_WIDTH), random.nextInt(ARMY_HEIGHT));
                while (coordinatesList.contains(coordinates)){
                    coordinates.setxCoord(random.nextInt(ARMY_WIDTH));
                    coordinates.setyCoord(random.nextInt(ARMY_HEIGHT));
                }
                coordinatesList.add(coordinates);
                Unit newUnit = new Unit(
                        unitName,
                        unit.getUnitType(),
                        unit.getHealth(),
                        unit.getBaseAttack(),
                        unit.getCost(),
                        unit.getAttackType(),
                        unit.getAttackBonuses(),
                        unit.getDefenceBonuses(),
                        coordinates.getxCoord(),
                        coordinates.getyCoord()
                );

                unitsInArmy.add(newUnit);
                unitCounter++;
            }
            remainingPoints -= maxUnits * unit.getCost();

        }

        army.setUnits(unitsInArmy);
        return army;
    }

    private static class Coordinates{
        private int x;
        private int y;
        public Coordinates(int x, int y){
            this.x = x;
            this.y = y;
        }
        public void setxCoord(int x){
            this.x = x;
        }
        public void setyCoord(int y){
            this.y = y;
        }
        public int getxCoord(){
            return x;
        }
        public int getyCoord(){
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Coordinates coordinates){
                return x == coordinates.getxCoord() && y == coordinates.getyCoord();
            }
            return false;
        }
        @Override
        public int hashCode(){
            return x * 31 + y;
        }
    }
}