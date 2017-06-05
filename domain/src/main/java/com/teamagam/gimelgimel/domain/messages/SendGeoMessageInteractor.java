package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeometryVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

@AutoFactory
public class SendGeoMessageInteractor extends SendMessageInteractor<MessageGeo> {

  private final String mMessageText;
  private final Geometry mMessageGeometry;
  private final String mMessageType;

  SendGeoMessageInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided UserPreferencesRepository userPreferences,
      @Provided MessagesRepository messagesRepository,
      @Provided MessageNotifications messageNotifications,
      String text,
      Geometry geometry,
      String type) {
    super(threadExecutor, userPreferences, messageNotifications, messagesRepository);
    mMessageText = text;
    mMessageGeometry = geometry;
    mMessageType = type;
  }

  @Override
  protected MessageGeo createMessage(String senderId) {
    CreateGeoEntityVisitor visitor = new CreateGeoEntityVisitor();
    mMessageGeometry.accept(visitor);
    return new MessageGeo(null, senderId, null, visitor.getResult());
  }

  private class CreateGeoEntityVisitor implements IGeometryVisitor {

    private static final String NOT_USED_ID = "not_used";

    private GeoEntity mResult;

    public GeoEntity getResult() {
      return mResult;
    }

    @Override
    public void visit(PointGeometry pointGeometry) {
      PointSymbol symbol = new PointSymbol(false, mMessageType);
      mResult = new PointEntity(NOT_USED_ID, mMessageText.trim(), pointGeometry, symbol);
    }

    @Override
    public void visit(Polygon polygon) {
      PolygonSymbol symbol = new PolygonSymbol(false);
      mResult = new PolygonEntity(NOT_USED_ID, mMessageText.trim(), polygon, symbol);
    }

    @Override
    public void visit(Polyline polyline) {
      PolylineSymbol symbol = new PolylineSymbol(false);
      mResult = new PolylineEntity(NOT_USED_ID, mMessageText.trim(), polyline, symbol);
    }
  }
}