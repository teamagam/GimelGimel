package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeometryVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

@AutoFactory
public class QueueGeoMessageForSendingInteractor extends QueueMessageForSendingInteractor {

  private final String mMessageText;
  private final Geometry mMessageGeometry;
  private final Symbol mMessageSymbol;

  QueueGeoMessageForSendingInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided UserPreferencesRepository userPreferences,
      @Provided OutGoingMessagesQueue outGoingMessagesQueue,
      String text,
      Geometry geometry,
      Symbol messageSymbol) {
    super(threadExecutor, userPreferences, outGoingMessagesQueue);
    mMessageText = text;
    mMessageGeometry = geometry;
    mMessageSymbol = messageSymbol;
  }

  @Override
  protected OutGoingChatMessage createMessage(String senderId) {
    CreateGeoEntityVisitor visitor = new CreateGeoEntityVisitor();
    mMessageGeometry.accept(visitor);

    return new ChatMessage(null, senderId, null, new TextFeature(mMessageText),
        new GeoFeature(visitor.getResult()));
  }

  private class CreateGeoEntityVisitor implements GeometryVisitor {

    private static final String NOT_USED_ID = "not_used";

    private GeoEntity mResult;

    public GeoEntity getResult() {
      return mResult;
    }

    @Override
    public void visit(PointGeometry pointGeometry) {
      mResult = new PointEntity(NOT_USED_ID, pointGeometry, (PointSymbol) mMessageSymbol);
    }

    @Override
    public void visit(Polygon polygon) {
      mResult = new PolygonEntity(NOT_USED_ID, polygon, (PolygonSymbol) mMessageSymbol);
    }

    @Override
    public void visit(Polyline polyline) {
      mResult = new PolylineEntity(NOT_USED_ID, polyline, (PolylineSymbol) mMessageSymbol);
    }
  }
}