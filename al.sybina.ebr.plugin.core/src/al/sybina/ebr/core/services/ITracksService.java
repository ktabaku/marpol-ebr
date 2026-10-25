package al.sybina.ebr.core.services;

public interface ITracksService extends IIbrCoreService{

	void openTrack(String fileName);

	String[] getFileExtensions();

	void saveAs(String fileName);

}
