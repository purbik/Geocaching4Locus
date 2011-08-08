package geocaching.api.data;

import geocaching.api.data.type.CacheType;
import geocaching.api.data.type.ContainerType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import menion.android.locus.addon.publiclib.geoData.Point;
import menion.android.locus.addon.publiclib.geoData.PointGeocachingData;
import android.location.Location;

public class SimpleGeocache {
	private final String geoCode;
	private final String name;
	private final double longitude;
	private final double latitude;
	private final CacheType cacheType;
	private final float difficultyRating;
	private final float terrainRating;
	private final String authorGuid;
	private final String authorName;
	private final boolean available;
	private final boolean archived;
	private final boolean premiumListing;
	private final String countryName;
	private final String stateName;
	private final Date created;
	private final String contactName;
	private final ContainerType containerType;
	private final int trackableCount;
	private final boolean found;

	private static final DateFormat GPX_TIME_FMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	public SimpleGeocache(String geoCode, String name, double longitude,
			double latitude, CacheType cacheType, float difficultyRating,
			float terrainRating, String authorGuid, String authorName,
			boolean available, boolean archived, boolean premiumListing,
			String countryName, String stateName, Date created,
			String contactName, ContainerType containerType,
			int trackableCount, boolean found) {
		this.geoCode = geoCode;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.cacheType = cacheType;
		this.difficultyRating = difficultyRating;
		this.terrainRating = terrainRating;
		this.authorGuid = authorGuid;
		this.authorName = authorName;
		this.available = available;
		this.archived = archived;
		this.premiumListing = premiumListing;
		this.countryName = countryName;
		this.stateName = stateName;
		this.created = created;
		this.contactName = contactName;
		this.containerType = containerType;
		this.trackableCount = trackableCount;
		this.found = found;
	}

	public String getGeoCode() {
		return geoCode;
	}

	public String getName() {
		return name;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public CacheType getCacheType() {
		return cacheType;
	}

	public float getDifficultyRating() {
		return difficultyRating;
	}

	public float getTerrainRating() {
		return terrainRating;
	}

	public String getAuthorGuid() {
		return authorGuid;
	}

	public String getAuthorName() {
		return authorName;
	}

	public boolean isAvailable() {
		return available;
	}

	public boolean isArchived() {
		return archived;
	}

	public boolean isPremiumListing() {
		return premiumListing;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getStateName() {
		return stateName;
	}

	public Date getCreated() {
		return created;
	}

	public String getContactName() {
		return contactName;
	}

	public ContainerType getContainerType() {
		return containerType;
	}

	public int getTrackableCount() {
		return trackableCount;
	}

	public boolean isFound() {
		return found;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Method m : getClass().getMethods()) {
			if ((!m.getName().startsWith("get") && !m.getName().startsWith("is")) ||
					m.getParameterTypes().length != 0 ||
					void.class.equals(m.getReturnType()))
				continue;

			sb.append(m.getName());
			sb.append(':');
			try {
				sb.append(m.invoke(this, new Object[0]));
			} catch (Exception e) {
			}
			sb.append(", ");
		}
		return sb.toString();
	}

	public static SimpleGeocache load(DataInputStream dis) throws IOException {
		return new SimpleGeocache(
				dis.readUTF(),
				dis.readUTF(),
				dis.readDouble(),
				dis.readDouble(),
				CacheType.parseCacheType(dis.readUTF()),
				dis.readFloat(),
				dis.readFloat(),
				dis.readUTF(),
				dis.readUTF(),
				dis.readBoolean(),
				dis.readBoolean(),
				dis.readBoolean(),
				dis.readUTF(),
				dis.readUTF(),
				new Date(dis.readLong()),
				dis.readUTF(),
				ContainerType.parseContainerType(dis.readUTF()),
				dis.readInt(),
				dis.readBoolean());
	}

	public void store(DataOutputStream dos) throws IOException {
		dos.writeUTF(getGeoCode());
		dos.writeUTF(getName());
		dos.writeDouble(getLongitude());
		dos.writeDouble(getLatitude());
		dos.writeUTF(getCacheType().toString());
		dos.writeFloat(getDifficultyRating());
		dos.writeFloat(getTerrainRating());
		dos.writeUTF(getAuthorGuid());
		dos.writeUTF(getAuthorName());
		dos.writeBoolean(isAvailable());
		dos.writeBoolean(isArchived());
		dos.writeBoolean(isPremiumListing());
		dos.writeUTF(getCountryName());
		dos.writeUTF(getStateName());
		dos.writeLong(getCreated().getTime());
		dos.writeUTF(getContactName());
		dos.writeUTF(getContainerType().toString());
		dos.writeInt(getTrackableCount());
		dos.writeBoolean(isFound());
	}

	public Point toPoint() {
		Location loc = new Location(getClass().getName());
		loc.setLatitude(latitude);
		loc.setLongitude(longitude);

		Point p = new Point(name, loc);

		PointGeocachingData d = new PointGeocachingData();
		d.cacheID = geoCode;
		d.name = name;
		d.type = cacheType.getId();
		d.difficulty = difficultyRating;
		d.terrain = terrainRating;
		d.owner = authorName;
		d.placedBy = contactName;
		d.available = available;
		d.archived = archived;
		d.premiumOnly = premiumListing;
		d.country = countryName;
		d.state = stateName;
		d.hidden = GPX_TIME_FMT.format(created);
		d.exported = GPX_TIME_FMT.format(new Date());
		d.container = containerType.getId();

		p.setGeocachingData(d);
		return p;
	}
}
