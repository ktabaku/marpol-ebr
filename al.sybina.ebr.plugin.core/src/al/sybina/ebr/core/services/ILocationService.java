package al.sybina.ebr.core.services;

import al.sybina.ebr.core.geo.Position;

public interface ILocationService extends IIbrCoreService {

	String getNearestLocation(Position startPos);

}
