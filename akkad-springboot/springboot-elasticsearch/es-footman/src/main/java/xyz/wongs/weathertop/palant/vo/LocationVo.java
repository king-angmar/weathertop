package xyz.wongs.weathertop.palant.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.location.entity.Location;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationVo<T> {

    /**
     * idxName : idx_location
     * elasticEntity : {"id":1,"location":{"location_id":"143831","flag":"Y","local_code":"11","local_name":"市","lv":2,"sup_local_code":"0","url":"11.html"}}
     */

    private String idxName;
    private LocationEntity locationEntity;

    public static class LocationEntity {
        private String id;
        private Location location;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

}
