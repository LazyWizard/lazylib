package org.lazywizard.lazylib;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains {@link CollectionFilter}s to be used with
 * {@link CollectionUtils#filter(java.util.Collection,
 * org.lazywizard.lazylib.CollectionFilters.CollectionFilter)}.
 *
 * @author LazyWizard
 * @since 1.7
 */
// TODO: Add this to the changelog
public class CollectionFilters
{
    public static class EntitiesInRangeOfVector implements CollectionFilter<CombatEntityAPI>
    {
        private final Vector2f location;
        private final float maximumRangeSquared;

        public EntitiesInRangeOfVector(Vector2f location, float maximumRange)
        {
            this.location = location;
            this.maximumRangeSquared = maximumRange * maximumRange;
        }

        @Override
        public boolean accept(CombatEntityAPI entity)
        {
            return (MathUtils.getDistanceSquared(location, entity.getLocation())
                    <= maximumRangeSquared);
        }
    }

    public static class FleetsInRangeOfVector implements CollectionFilter<CampaignFleetAPI>
    {
        private final Vector2f location;
        private final float maximumRangeSquared;

        public FleetsInRangeOfVector(Vector2f location, float maximumRange)
        {
            this.location = location;
            this.maximumRangeSquared = maximumRange * maximumRange;
        }

        @Override
        public boolean accept(CampaignFleetAPI fleet)
        {
            return (MathUtils.getDistanceSquared(location, fleet.getLocation())
                    <= maximumRangeSquared);
        }
    }

    public static interface CollectionFilter<T>
    {
        public boolean accept(T t);
    }

    private CollectionFilters()
    {
    }
}
