// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/platform/auth/java/ServerExpiredNotice.proto

package com.navinfo.opentsp.platform.location.protocol.platform.auth;

public final class LCServerExpiredNotice {
  private LCServerExpiredNotice() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface ServerExpiredNoticeOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional int64 mainServerIdentify = 1;
    boolean hasMainServerIdentify();
    long getMainServerIdentify();
    
    // repeated int64 serverIdentifies = 2;
    java.util.List<Long> getServerIdentifiesList();
    int getServerIdentifiesCount();
    long getServerIdentifies(int index);
  }
  public static final class ServerExpiredNotice extends
      com.google.protobuf.GeneratedMessage
      implements ServerExpiredNoticeOrBuilder {
    // Use ServerExpiredNotice.newBuilder() to construct.
    private ServerExpiredNotice(Builder builder) {
      super(builder);
    }
    private ServerExpiredNotice(boolean noInit) {}
    
    private static final ServerExpiredNotice defaultInstance;
    public static ServerExpiredNotice getDefaultInstance() {
      return defaultInstance;
    }
    
    public ServerExpiredNotice getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCServerExpiredNotice.internal_static_ServerExpiredNotice_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCServerExpiredNotice.internal_static_ServerExpiredNotice_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional int64 mainServerIdentify = 1;
    public static final int MAINSERVERIDENTIFY_FIELD_NUMBER = 1;
    private long mainServerIdentify_;
    public boolean hasMainServerIdentify() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getMainServerIdentify() {
      return mainServerIdentify_;
    }
    
    // repeated int64 serverIdentifies = 2;
    public static final int SERVERIDENTIFIES_FIELD_NUMBER = 2;
    private java.util.List<Long> serverIdentifies_;
    public java.util.List<Long>
        getServerIdentifiesList() {
      return serverIdentifies_;
    }
    public int getServerIdentifiesCount() {
      return serverIdentifies_.size();
    }
    public long getServerIdentifies(int index) {
      return serverIdentifies_.get(index);
    }
    
    private void initFields() {
      mainServerIdentify_ = 0L;
      serverIdentifies_ = java.util.Collections.emptyList();;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt64(1, mainServerIdentify_);
      }
      for (int i = 0; i < serverIdentifies_.size(); i++) {
        output.writeInt64(2, serverIdentifies_.get(i));
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(1, mainServerIdentify_);
      }
      {
        int dataSize = 0;
        for (int i = 0; i < serverIdentifies_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt64SizeNoTag(serverIdentifies_.get(i));
        }
        size += dataSize;
        size += 1 * getServerIdentifiesList().size();
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
    
    public static ServerExpiredNotice parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static ServerExpiredNotice parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static ServerExpiredNotice parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static ServerExpiredNotice parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static ServerExpiredNotice parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static ServerExpiredNotice parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static ServerExpiredNotice parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static ServerExpiredNotice parseDelimitedFrom(
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
    public static ServerExpiredNotice parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static ServerExpiredNotice parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ServerExpiredNotice prototype) {
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
       implements ServerExpiredNoticeOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCServerExpiredNotice.internal_static_ServerExpiredNotice_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCServerExpiredNotice.internal_static_ServerExpiredNotice_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.platform.auth.LCServerExpiredNotice.ServerExpiredNotice.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        mainServerIdentify_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        serverIdentifies_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ServerExpiredNotice.getDescriptor();
      }
      
      public ServerExpiredNotice getDefaultInstanceForType() {
        return ServerExpiredNotice.getDefaultInstance();
      }
      
      public ServerExpiredNotice build() {
        ServerExpiredNotice result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private ServerExpiredNotice buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        ServerExpiredNotice result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public ServerExpiredNotice buildPartial() {
        ServerExpiredNotice result = new ServerExpiredNotice(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.mainServerIdentify_ = mainServerIdentify_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          serverIdentifies_ = java.util.Collections.unmodifiableList(serverIdentifies_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.serverIdentifies_ = serverIdentifies_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ServerExpiredNotice) {
          return mergeFrom((ServerExpiredNotice)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(ServerExpiredNotice other) {
        if (other == ServerExpiredNotice.getDefaultInstance()) return this;
        if (other.hasMainServerIdentify()) {
          setMainServerIdentify(other.getMainServerIdentify());
        }
        if (!other.serverIdentifies_.isEmpty()) {
          if (serverIdentifies_.isEmpty()) {
            serverIdentifies_ = other.serverIdentifies_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureServerIdentifiesIsMutable();
            serverIdentifies_.addAll(other.serverIdentifies_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
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
            case 8: {
              bitField0_ |= 0x00000001;
              mainServerIdentify_ = input.readInt64();
              break;
            }
            case 16: {
              ensureServerIdentifiesIsMutable();
              serverIdentifies_.add(input.readInt64());
              break;
            }
            case 18: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              while (input.getBytesUntilLimit() > 0) {
                addServerIdentifies(input.readInt64());
              }
              input.popLimit(limit);
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional int64 mainServerIdentify = 1;
      private long mainServerIdentify_ ;
      public boolean hasMainServerIdentify() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getMainServerIdentify() {
        return mainServerIdentify_;
      }
      public Builder setMainServerIdentify(long value) {
        bitField0_ |= 0x00000001;
        mainServerIdentify_ = value;
        onChanged();
        return this;
      }
      public Builder clearMainServerIdentify() {
        bitField0_ = (bitField0_ & ~0x00000001);
        mainServerIdentify_ = 0L;
        onChanged();
        return this;
      }
      
      // repeated int64 serverIdentifies = 2;
      private java.util.List<Long> serverIdentifies_ = java.util.Collections.emptyList();;
      private void ensureServerIdentifiesIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          serverIdentifies_ = new java.util.ArrayList<Long>(serverIdentifies_);
          bitField0_ |= 0x00000002;
         }
      }
      public java.util.List<Long>
          getServerIdentifiesList() {
        return java.util.Collections.unmodifiableList(serverIdentifies_);
      }
      public int getServerIdentifiesCount() {
        return serverIdentifies_.size();
      }
      public long getServerIdentifies(int index) {
        return serverIdentifies_.get(index);
      }
      public Builder setServerIdentifies(
          int index, long value) {
        ensureServerIdentifiesIsMutable();
        serverIdentifies_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addServerIdentifies(long value) {
        ensureServerIdentifiesIsMutable();
        serverIdentifies_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllServerIdentifies(
          Iterable<? extends Long> values) {
        ensureServerIdentifiesIsMutable();
        super.addAll(values, serverIdentifies_);
        onChanged();
        return this;
      }
      public Builder clearServerIdentifies() {
        serverIdentifies_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:ServerExpiredNotice)
    }
    
    static {
      defaultInstance = new ServerExpiredNotice(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:ServerExpiredNotice)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ServerExpiredNotice_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ServerExpiredNotice_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n7core/proto/platform/auth/java/ServerEx" +
      "piredNotice.proto\"K\n\023ServerExpiredNotice" +
      "\022\032\n\022mainServerIdentify\030\001 \001(\003\022\030\n\020serverId" +
      "entifies\030\002 \003(\003BU\n<com.navinfo.opentsp.pl" +
      "atform.location.protocol.platform.authB\025" +
      "LCServerExpiredNotice"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_ServerExpiredNotice_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_ServerExpiredNotice_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ServerExpiredNotice_descriptor,
              new String[] { "MainServerIdentify", "ServerIdentifies", },
              ServerExpiredNotice.class,
              ServerExpiredNotice.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
