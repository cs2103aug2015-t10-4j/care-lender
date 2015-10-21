package carelender.model;

import java.io.BufferedReader;
/**
 * Handles all database and file saving
 */
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.*;
import com.google.gson.Gson;
import carelender.model.AppSettings.SettingName;
import carelender.model.UndoStep.UndoType;
import carelender.model.data.*;

public class Model {

	private static Model singleton = null;

	public static Model getInstance() {
		if (singleton == null) {
			singleton = new Model();
		}
		return singleton;
	}

	private String filename;
	private EventList events;
	private ArrayList<EventList> cache;
	private ArrayList<String> storage;
	private static Logger log;

	private int currentUid;

	private Model() {
		log = Logger.getLogger(Model.class.getName());
		File file = new File("events.dat");
		events = new EventList();
		events = getFromFile("events.dat");
		currentUid = 1;
		if (AppSettings.getInstance().getIntSetting(SettingName.CURRENT_INDEX) != null) {
			currentUid = AppSettings.getInstance().getIntSetting(SettingName.CURRENT_INDEX);
		}
		System.out.println("CurrentID" + currentUid);
	}

	public boolean addEvent(Event eventObj) {
		eventObj.setUid(currentUid += 1);// Set incremented UID to Event
		events.add(eventObj);
		AppSettings.getInstance().setIntSetting(SettingName.CURRENT_INDEX, currentUid);
		updateUndoManager(eventObj, UndoType.ADD);
		System.out.println("Added UID:" + currentUid + "Event Name: " + eventObj.getName());
		return saveToFile("events.dat", events);
	}

	public EventList retrieveEvent() {
		return events;
	}

	public boolean updateEvent(Event eventObj) {
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getUid() == eventObj.getUid()) {
				updateUndoManager(events.get(i), UndoType.UPDATE);
				events.remove(i);
				events.add(eventObj);
				return saveToFile("events.dat", events);
			}
		}
		return false;
	}

	// Delete single Event
	public void deleteEvent(Event eventObj) {
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getUid() == eventObj.getUid()) {
				updateUndoManager(events.get(i), UndoType.DELETE);
				events.remove(i);
			}
			saveToFile("events.dat", events);
		}
	}

	// Delete multiple Events
	public void deleteEvent(EventList eventList) {
		EventList deletedEventList = new EventList();
		for (int i = 0; i < events.size(); i++) {
			for (Event eventObj : eventList) {
				if (events.get(i).getUid() == eventObj.getUid()) {
					deletedEventList.add(events.get(i));
					events.remove(i);
				}
			}
		}
		updateUndoManager(deletedEventList);
		saveToFile("events.dat", events);
	}

	public void undoAddedEvent(EventList eventList) {
		for (int i = 0; i < events.size(); i++) {
			for (Event eventObj : eventList) {
				if (events.get(i).getUid() == eventObj.getUid()) {
					events.remove(i);
				}
			}
		}
		saveToFile("events.dat", events);
	}

	public void undoUpdatedEvent(EventList eventList) {

	}

	public void undoDeletedEvent(EventList eventList) {
		for (int i = 0; i < eventList.size(); i++) {
			events.add(eventList.get(i));
		}
		saveToFile("events.dat", events);
	}

	private void updateUndoManager(Event eventObj, UndoStep.UndoType type) {
		EventList eventList = new EventList();
		eventList.add(eventObj);
		switch (type) {
		case ADD:
			UndoManager.getInstance().add(eventList);
			break;
		case DELETE:
			UndoManager.getInstance().delete(eventList);
			break;
		case UPDATE:
			UndoManager.getInstance().update(eventList);
			break;
		default:
			break;
		}
	}

	private void updateUndoManager(EventList eventList) {
		UndoManager.getInstance().delete(eventList);
	}

	private boolean saveToFile(String filename, EventList eventList) {
		try {
			PrintWriter printWriter = new PrintWriter(filename);
			Gson gson = new Gson();
			String json = gson.toJson(eventList);

			printWriter.println(json);
			printWriter.flush();
			printWriter.close();

			return true;
		} catch (IOException ioe) {
			log.log(Level.FINE, "Failed to save event to file");
			return false;
		}
	}

	private EventList getFromFile(String filename) {
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			Gson gson = new Gson();
			String json = bufferedReader.readLine();
			EventList eventList = new EventList();
			eventList = gson.fromJson(json, EventList.class);

			bufferedReader.close();
			fileReader.close();

			return eventList;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
		}
		return new EventList();
	}
}
