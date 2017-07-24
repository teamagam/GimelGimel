package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.ImageSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SymbolToStyleMapper {

  @Inject
  public SymbolToStyleMapper() {
  }

  public GeoFeatureEntity.Style transform(Symbol symbol) {
    StyleCreationVisitor visitor = new StyleCreationVisitor();
    symbol.accept(visitor);
    return visitor.getStyle();
  }

  public class StyleCreationVisitor implements ISymbolVisitor {

    private GeoFeatureEntity.Style mStyle;

    public StyleCreationVisitor() {
      mStyle = new GeoFeatureEntity.Style();
    }

    public GeoFeatureEntity.Style getStyle() {
      return mStyle;
    }

    @Override
    public void visit(PointSymbol symbol) {
      mStyle.iconId = symbol.getIconId();
      mStyle.iconTint = symbol.getTintColor();
    }

    @Override
    public void visit(ImageSymbol symbol) {
      //Should not be called
    }

    @Override
    public void visit(UserSymbol symbol) {
      //Should not be called
    }

    @Override
    public void visit(AlertPointSymbol symbol) {
      //Should not be called
    }

    @Override
    public void visit(AlertPolygonSymbol symbol) {
      //Should not be called
    }

    @Override
    public void visit(PolygonSymbol symbol) {
      fillPolylineStyle(symbol);
      mStyle.fillColor = symbol.getFillColor();
    }

    @Override
    public void visit(PolylineSymbol symbol) {
      fillPolylineStyle(symbol);
    }

    private void fillPolylineStyle(PolylineSymbol symbol) {
      mStyle.borderColor = symbol.getBorderColor();
      mStyle.borderStyle = symbol.getBorderStyle();
    }
  }
}
