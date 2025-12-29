package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int ARMY_WIDTH = 3;
    private static final int ARMY_HEIGHT = 21;
    private static final int MAX_UNITS_PER_TYPE = 11;
    private static final int TOTAL_POSITIONS = ARMY_WIDTH * ARMY_HEIGHT;
    Random random = new Random();

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        //
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

        List<Coordinates> allCoordinates = generateAllShuffledCoordinates();
        int coordIndex = 0;

        List<Unit> unitsInArmy = new ArrayList<>();

        for (Unit unit : unitList) {

            int maxUnits = Math.min(MAX_UNITS_PER_TYPE, remainingPoints / unit.getCost());

            for (int i = 0; i < maxUnits; i++) {
                String unitName = unit.getName() + " " + (unitCounter + 1);

                Coordinates coordinates = allCoordinates.get(coordIndex++);

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

    private List<Coordinates> generateAllShuffledCoordinates() {
        List<Coordinates> allCoords = new ArrayList<>(TOTAL_POSITIONS);

        // Создаем все возможные координаты
        for (int x = 0; x < ARMY_WIDTH; x++) {
            for (int y = 0; y < ARMY_HEIGHT; y++) {
                allCoords.add(new Coordinates(x, y));
            }
        }

        // Перемешиваем координаты для случайного распределения
        Collections.shuffle(allCoords);

        return allCoords;
    }

    private static class Coordinates {
        private int x;
        private int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setxCoord(int x) {
            this.x = x;
        }

        public void setyCoord(int y) {
            this.y = y;
        }

        public int getxCoord() {
            return x;
        }

        public int getyCoord() {
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Coordinates coordinates) {
                return x == coordinates.getxCoord() && y == coordinates.getyCoord();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return x * 31 + y;
        }
    }
}