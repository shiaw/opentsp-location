// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/webservice/originaldata/java/CrossAreaTracks.proto

package com.navinfo.opentsp.platform.location.protocol.webservice.originaldata;

public final class LCCrossAreaTracks {
  private LCCrossAreaTracks() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface CrossAreaTracksOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // repeated .TerminalTracking tracks = 1;
    java.util.List<LCTerminalTracking.TerminalTracking>
        getTracksList();
    LCTerminalTracking.TerminalTracking getTracks(int index);
    int getTracksCount();
    java.util.List<? extends LCTerminalTracking.TerminalTrackingOrBuilder>
        getTracksOrBuilderList();
    LCTerminalTracking.TerminalTrackingOrBuilder getTracksOrBuilder(
            int index);
  }
  public static final class CrossAreaTracks extends
      com.google.protobuf.GeneratedMessage
      implements CrossAreaTracksOrBuilder {
    // Use CrossAreaTracks.newBuilder() to construct.
    private CrossAreaTracks(Builder builder) {
      super(builder);
    }
    private CrossAreaTracks(boolean noInit) {}
    
    private static final CrossAreaTracks defaultInstance;
    public static CrossAreaTracks getDefaultInstance() {
      return defaultInstance;
    }
    
    public CrossAreaTracks getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCCrossAreaTracks.internal_static_CrossAreaTracks_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCCrossAreaTracks.internal_static_CrossAreaTracks_fieldAccessorTable;
    }
    
    // repeated .TerminalTracking tracks = 1;
    public static final int TRACKS_FIELD_NUMBER = 1;
    private java.util.List<LCTerminalTracking.TerminalTracking> tracks_;
    public java.util.List<LCTerminalTracking.TerminalTracking> getTracksList() {
      return tracks_;
    }
    public java.util.List<? extends LCTerminalTracking.TerminalTrackingOrBuilder>
        getTracksOrBuilderList() {
      return tracks_;
    }
    public int getTracksCount() {
      return tracks_.size();
    }
    public LCTerminalTracking.TerminalTracking getTracks(int index) {
      return tracks_.get(index);
    }
    public LCTerminalTracking.TerminalTrackingOrBuilder getTracksOrBuilder(
        int index) {
      return tracks_.get(index);
    }
    
    private void initFields() {
      tracks_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      for (int i = 0; i < getTracksCount(); i++) {
        if (!getTracks(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      for (int i = 0; i < tracks_.size(); i++) {
        output.writeMessage(1, tracks_.get(i));
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      for (int i = 0; i < tracks_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, tracks_.get(i));
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @Override
    protected Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static CrossAreaTracks parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static CrossAreaTracks parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static CrossAreaTracks parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static CrossAreaTracks parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static CrossAreaTracks parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static CrossAreaTracks parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static CrossAreaTracks parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static CrossAreaTracks parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static CrossAreaTracks parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static CrossAreaTracks parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CrossAreaTracks prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements CrossAreaTracksOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCCrossAreaTracks.internal_static_CrossAreaTracks_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCCrossAreaTracks.internal_static_CrossAreaTracks_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCCrossAreaTracks.CrossAreaTracks.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getTracksFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        if (tracksBuilder_ == null) {
          tracks_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          tracksBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CrossAreaTracks.getDescriptor();
      }
      
      public CrossAreaTracks getDefaultInstanceForType() {
        return CrossAreaTracks.getDefaultInstance();
      }
      
      public CrossAreaTracks build() {
        CrossAreaTracks result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private CrossAreaTracks buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        CrossAreaTracks result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public CrossAreaTracks buildPartial() {
        CrossAreaTracks result = new CrossAreaTracks(this);
        int from_bitField0_ = bitField0_;
        if (tracksBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            tracks_ = java.util.Collections.unmodifiableList(tracks_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.tracks_ = tracks_;
        } else {
          result.tracks_ = tracksBuilder_.build();
        }
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CrossAreaTracks) {
          return mergeFrom((CrossAreaTracks)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(CrossAreaTracks other) {
        if (other == CrossAreaTracks.getDefaultInstance()) return this;
        if (tracksBuilder_ == null) {
          if (!other.tracks_.isEmpty()) {
            if (tracks_.isEmpty()) {
              tracks_ = other.tracks_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureTracksIsMutable();
              tracks_.addAll(other.tracks_);
            }
            onChanged();
          }
        } else {
          if (!other.tracks_.isEmpty()) {
            if (tracksBuilder_.isEmpty()) {
              tracksBuilder_.dispose();
              tracksBuilder_ = null;
              tracks_ = other.tracks_;
              bitField0_ = (bitField0_ & ~0x00000001);
              tracksBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getTracksFieldBuilder() : null;
            } else {
              tracksBuilder_.addAllMessages(other.tracks_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        for (int i = 0; i < getTracksCount(); i++) {
          if (!getTracks(i).isInitialized()) {
            
            return false;
          }
        }
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 10: {
              LCTerminalTracking.TerminalTracking.Builder subBuilder = LCTerminalTracking.TerminalTracking.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addTracks(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // repeated .TerminalTracking tracks = 1;
      private java.util.List<LCTerminalTracking.TerminalTracking> tracks_ =
        java.util.Collections.emptyList();
      private void ensureTracksIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          tracks_ = new java.util.ArrayList<LCTerminalTracking.TerminalTracking>(tracks_);
          bitField0_ |= 0x00000001;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          LCTerminalTracking.TerminalTracking, LCTerminalTracking.TerminalTracking.Builder, LCTerminalTracking.TerminalTrackingOrBuilder> tracksBuilder_;
      
      public java.util.List<LCTerminalTracking.TerminalTracking> getTracksList() {
        if (tracksBuilder_ == null) {
          return java.util.Collections.unmodifiableList(tracks_);
        } else {
          return tracksBuilder_.getMessageList();
        }
      }
      public int getTracksCount() {
        if (tracksBuilder_ == null) {
          return tracks_.size();
        } else {
          return tracksBuilder_.getCount();
        }
      }
      public LCTerminalTracking.TerminalTracking getTracks(int index) {
        if (tracksBuilder_ == null) {
          return tracks_.get(index);
        } else {
          return tracksBuilder_.getMessage(index);
        }
      }
      public Builder setTracks(
          int index, LCTerminalTracking.TerminalTracking value) {
        if (tracksBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTracksIsMutable();
          tracks_.set(index, value);
          onChanged();
        } else {
          tracksBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setTracks(
          int index, LCTerminalTracking.TerminalTracking.Builder builderForValue) {
        if (tracksBuilder_ == null) {
          ensureTracksIsMutable();
          tracks_.set(index, builderForValue.build());
          onChanged();
        } else {
          tracksBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addTracks(LCTerminalTracking.TerminalTracking value) {
        if (tracksBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTracksIsMutable();
          tracks_.add(value);
          onChanged();
        } else {
          tracksBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addTracks(
          int index, LCTerminalTracking.TerminalTracking value) {
        if (tracksBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTracksIsMutable();
          tracks_.add(index, value);
          onChanged();
        } else {
          tracksBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addTracks(
          LCTerminalTracking.TerminalTracking.Builder builderForValue) {
        if (tracksBuilder_ == null) {
          ensureTracksIsMutable();
          tracks_.add(builderForValue.build());
          onChanged();
        } else {
          tracksBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addTracks(
          int index, LCTerminalTracking.TerminalTracking.Builder builderForValue) {
        if (tracksBuilder_ == null) {
          ensureTracksIsMutable();
          tracks_.add(index, builderForValue.build());
          onChanged();
        } else {
          tracksBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllTracks(
          Iterable<? extends LCTerminalTracking.TerminalTracking> values) {
        if (tracksBuilder_ == null) {
          ensureTracksIsMutable();
          super.addAll(values, tracks_);
          onChanged();
        } else {
          tracksBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearTracks() {
        if (tracksBuilder_ == null) {
          tracks_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          tracksBuilder_.clear();
        }
        return this;
      }
      public Builder removeTracks(int index) {
        if (tracksBuilder_ == null) {
          ensureTracksIsMutable();
          tracks_.remove(index);
          onChanged();
        } else {
          tracksBuilder_.remove(index);
        }
        return this;
      }
      public LCTerminalTracking.TerminalTracking.Builder getTracksBuilder(
          int index) {
        return getTracksFieldBuilder().getBuilder(index);
      }
      public LCTerminalTracking.TerminalTrackingOrBuilder getTracksOrBuilder(
          int index) {
        if (tracksBuilder_ == null) {
          return tracks_.get(index);  } else {
          return tracksBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends LCTerminalTracking.TerminalTrackingOrBuilder>
           getTracksOrBuilderList() {
        if (tracksBuilder_ != null) {
          return tracksBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(tracks_);
        }
      }
      public LCTerminalTracking.TerminalTracking.Builder addTracksBuilder() {
        return getTracksFieldBuilder().addBuilder(
            LCTerminalTracking.TerminalTracking.getDefaultInstance());
      }
      public LCTerminalTracking.TerminalTracking.Builder addTracksBuilder(
          int index) {
        return getTracksFieldBuilder().addBuilder(
            index, LCTerminalTracking.TerminalTracking.getDefaultInstance());
      }
      public java.util.List<LCTerminalTracking.TerminalTracking.Builder>
           getTracksBuilderList() {
        return getTracksFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          LCTerminalTracking.TerminalTracking, LCTerminalTracking.TerminalTracking.Builder, LCTerminalTracking.TerminalTrackingOrBuilder>
          getTracksFieldBuilder() {
        if (tracksBuilder_ == null) {
          tracksBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              LCTerminalTracking.TerminalTracking, LCTerminalTracking.TerminalTracking.Builder, LCTerminalTracking.TerminalTrackingOrBuilder>(
                  tracks_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          tracks_ = null;
        }
        return tracksBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:CrossAreaTracks)
    }
    
    static {
      defaultInstance = new CrossAreaTracks(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:CrossAreaTracks)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_CrossAreaTracks_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_CrossAreaTracks_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n=core/proto/webservice/originaldata/jav" +
      "a/CrossAreaTracks.proto\032)core/proto/comm" +
      "on/java/LocationData.proto\032>core/proto/w" +
      "ebservice/originaldata/java/TerminalTrac" +
      "king.proto\"4\n\017CrossAreaTracks\022!\n\006tracks\030" +
      "\001 \003(\0132\021.TerminalTrackingB[\nFcom.navinfo." +
      "opentsp.platform.location.protocol.webse" +
      "rvice.originaldataB\021LCCrossAreaTracks"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_CrossAreaTracks_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_CrossAreaTracks_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_CrossAreaTracks_descriptor,
              new String[] { "Tracks", },
              CrossAreaTracks.class,
              CrossAreaTracks.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.getDescriptor(),
          LCTerminalTracking.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
