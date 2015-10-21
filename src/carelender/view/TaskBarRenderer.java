package carelender.view;

import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.data.EventList;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Written by : Weizheng Lee 19/10/2015
 * This class contains static methods to help to render the calendar view
 */
public class TaskBarRenderer {
    private GraphicsContext gc;
    private double xPosition;
    private double yPosition;

    private double width;
    private double height;

    private double xPadding;
    private double yPadding;

	private TextRenderer timeText;
	private TextRenderer nameText;

	private double timeTextRatio;
	private double nameTextRatio;

	private Event event;

	public TaskBarRenderer() {
		this.timeText = new TextRenderer();
		this.nameText = new TextRenderer();
	}

	public void setContent (Event event) {
		this.event = event;

		Font font = Font.loadFont("file:res/monaco.ttf", this.height * nameTextRatio);
		this.nameText.setParams (gc, this.xPosition + this.xPadding, this.yPosition + this.yPadding,
				this.width - (this.xPadding * 2), font.getSize(), 0, 0, font, 0.6, 0);
		this.nameText.addTextEllipsis(this.event.getName());

		font = Font.loadFont("file:res/monaco.ttf", this.height * timeTextRatio);
		this.timeText.setParams (gc, this.xPosition + this.xPadding, this.yPosition + this.yPadding + (this.height * nameTextRatio),
				this.width - (this.xPadding * 2), font.getSize(), 0, 0, font, 0.6, 0);
		SimpleDateFormat timeFormat = new SimpleDateFormat("[dd MMM] [h : mm a]");
		this.timeText.addTextEllipsis(timeFormat.format(this.event.getEarliestDate()) + " to "
				+ timeFormat.format(this.event.getLatestDate()));
	}

	public void setPosition (double x, double y) {
		this.xPosition = x;
		this.yPosition = y;
	}

    public void setParams (GraphicsContext gc, double w, double h, double xPad, double yPad,
						   double timeTextRatio, double nameTextRatio) {
		this.gc = gc;

		this.width = w;
		this.height = h;

		this.xPadding = xPad;
		this.yPadding = yPad;

		this.timeTextRatio = timeTextRatio;
		this.nameTextRatio = nameTextRatio;
    }
	
	public void drawTaskBar (String backgroundColour, String textColour) {
		if (this.gc == null) {
			System.out.println("Error");
		} else {
			gc.setFill(Color.web(backgroundColour));
			gc.fillRect(this.xPosition, this.yPosition, this.width, this.height);

			this.nameText.drawText(backgroundColour, textColour);

			this.timeText.drawText(backgroundColour, textColour);
		}
	}
}