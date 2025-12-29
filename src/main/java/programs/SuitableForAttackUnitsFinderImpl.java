package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        // Ваше решение
        List<Unit> suitableUnits = new ArrayList<>();
        for (List<Unit> row : unitsByRow) {
            Set<Integer> occupiedUnits = new HashSet<>();
            for (Unit unit : row) {
                occupiedUnits.add(unit.getyCoordinate());
            }
            for (Unit unit : row) {
                if (isLeftArmyTarget) {
                    if (!occupiedUnits.contains(unit.getyCoordinate()-1)) {
                        suitableUnits.add(unit);
                    }
                }else{
                    if (!occupiedUnits.contains(unit.getyCoordinate()+1)) {
                        suitableUnits.add(unit);
                    }
                }
            }
        }
        return suitableUnits;
    }
}
