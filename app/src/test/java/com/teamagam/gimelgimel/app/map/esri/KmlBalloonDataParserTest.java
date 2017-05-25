package com.teamagam.gimelgimel.app.map.esri;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class KmlBalloonDataParserTest {

  private static final String SAMPLE_NAME = "יחידה אווירית";
  private static String NEW_LINE = "\n";
  private static String SAMPLE_KML_BALLOON_STYLE_FORMATTED = "<html>"
      + NEW_LINE
      + "<body>"
      + NEW_LINE
      + "<table border=\"2\" cols=\"2\" width=\"100%\">"
      + NEW_LINE
      + "<tr>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "category"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "value"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "</tr>"
      + NEW_LINE
      + "<tr>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "fill_opacity"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "0.4"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "</tr>"
      + NEW_LINE
      + "<tr>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "stroke-width"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "3"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "</tr>"
      + NEW_LINE
      + "<tr>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "organization"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "Lebanon"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "</tr>"
      + NEW_LINE
      + "<tr>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "type"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "unit"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "</tr>"
      + NEW_LINE
      + "<tr>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "description"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "</tr>"
      + NEW_LINE
      + "<tr>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + "name"
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "<td> <center>"
      + NEW_LINE
      + SAMPLE_NAME
      + NEW_LINE
      + "</center> </td>"
      + NEW_LINE
      + "</tr>"
      + NEW_LINE
      + "</table>"
      + NEW_LINE
      + "</body>"
      + NEW_LINE
      + "</html>";
  private KmlBalloonDataParser mKmlBalloonDataParser;

  @Before
  public void setUp() throws Exception {
    mKmlBalloonDataParser = new KmlBalloonDataParser(SAMPLE_KML_BALLOON_STYLE_FORMATTED);
  }

  @Test
  public void parseSampleDataName_appropriateName() throws Exception {
    //Act
    String actual = mKmlBalloonDataParser.parseName();

    //Assert
    Assert.assertThat(actual, is(SAMPLE_NAME));
  }

  @Test
  public void parseSampleDataDescription_emptyDescription() throws Exception {
    //Act
    String actual = mKmlBalloonDataParser.parseDescription();

    //Assert
    Assert.assertThat(actual, is(""));
  }
}