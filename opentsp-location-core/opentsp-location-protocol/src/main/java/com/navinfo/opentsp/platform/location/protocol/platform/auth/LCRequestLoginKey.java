// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/platform/auth/java/RequestLoginKey.proto

package com.navinfo.opentsp.platform.location.protocol.platform.auth;

public final class LCRequestLoginKey {
  private LCRequestLoginKey() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface RequestLoginKeyOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string name = 1;
    boolean hasName();
    String getName();
    
    // required string password = 2;
    boolean hasPassword();
    String getPassword();
    
    // required string version = 3;
    boolean hasVersion();
    String getVersion();
    
    // repeated .DistrictCode district = 4;
    java.util.List<com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode> getDistrictList();
    int getDistrictCount();
    com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode getDistrict(int index);
  }
  public static final class RequestLoginKey extends
      com.google.protobuf.GeneratedMessage
      implements RequestLoginKeyOrBuilder {
    // Use RequestLoginKey.newBuilder() to construct.
    private RequestLoginKey(Builder builder) {
      super(builder);
    }
    private RequestLoginKey(boolean noInit) {}
    
    private static final RequestLoginKey defaultInstance;
    public static RequestLoginKey getDefaultInstance() {
      return defaultInstance;
    }
    
    public RequestLoginKey getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCRequestLoginKey.internal_static_RequestLoginKey_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCRequestLoginKey.internal_static_RequestLoginKey_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string name = 1;
    public static final int NAME_FIELD_NUMBER = 1;
    private Object name_;
    public boolean hasName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getName() {
      Object ref = name_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          name_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getNameBytes() {
      Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required string password = 2;
    public static final int PASSWORD_FIELD_NUMBER = 2;
    private Object password_;
    public boolean hasPassword() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getPassword() {
      Object ref = password_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          password_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getPasswordBytes() {
      Object ref = password_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        password_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required string version = 3;
    public static final int VERSION_FIELD_NUMBER = 3;
    private Object version_;
    public boolean hasVersion() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getVersion() {
      Object ref = version_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          version_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getVersionBytes() {
      Object ref = version_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        version_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // repeated .DistrictCode district = 4;
    public static final int DISTRICT_FIELD_NUMBER = 4;
    private java.util.List<com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode> district_;
    public java.util.List<com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode> getDistrictList() {
      return district_;
    }
    public int getDistrictCount() {
      return district_.size();
    }
    public com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode getDistrict(int index) {
      return district_.get(index);
    }
    
    private void initFields() {
      name_ = "";
      password_ = "";
      version_ = "";
      district_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasName()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasPassword()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasVersion()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getPasswordBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getVersionBytes());
      }
      for (int i = 0; i < district_.size(); i++) {
        output.writeEnum(4, district_.get(i).getNumber());
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
          .computeBytesSize(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getPasswordBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getVersionBytes());
      }
      {
        int dataSize = 0;
        for (int i = 0; i < district_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeEnumSizeNoTag(district_.get(i).getNumber());
        }
        size += dataSize;
        size += 1 * district_.size();
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
    
    public static RequestLoginKey parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static RequestLoginKey parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static RequestLoginKey parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static RequestLoginKey parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static RequestLoginKey parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static RequestLoginKey parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static RequestLoginKey parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static RequestLoginKey parseDelimitedFrom(
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
    public static RequestLoginKey parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static RequestLoginKey parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(RequestLoginKey prototype) {
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
       implements RequestLoginKeyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCRequestLoginKey.internal_static_RequestLoginKey_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCRequestLoginKey.internal_static_RequestLoginKey_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.platform.auth.LCRequestLoginKey.RequestLoginKey.newBuilder()
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
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        password_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        version_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        district_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return RequestLoginKey.getDescriptor();
      }
      
      public RequestLoginKey getDefaultInstanceForType() {
        return RequestLoginKey.getDefaultInstance();
      }
      
      public RequestLoginKey build() {
        RequestLoginKey result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private RequestLoginKey buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        RequestLoginKey result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public RequestLoginKey buildPartial() {
        RequestLoginKey result = new RequestLoginKey(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.password_ = password_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.version_ = version_;
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          district_ = java.util.Collections.unmodifiableList(district_);
          bitField0_ = (bitField0_ & ~0x00000008);
        }
        result.district_ = district_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof RequestLoginKey) {
          return mergeFrom((RequestLoginKey)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(RequestLoginKey other) {
        if (other == RequestLoginKey.getDefaultInstance()) return this;
        if (other.hasName()) {
          setName(other.getName());
        }
        if (other.hasPassword()) {
          setPassword(other.getPassword());
        }
        if (other.hasVersion()) {
          setVersion(other.getVersion());
        }
        if (!other.district_.isEmpty()) {
          if (district_.isEmpty()) {
            district_ = other.district_;
            bitField0_ = (bitField0_ & ~0x00000008);
          } else {
            ensureDistrictIsMutable();
            district_.addAll(other.district_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasName()) {
          
          return false;
        }
        if (!hasPassword()) {
          
          return false;
        }
        if (!hasVersion()) {
          
          return false;
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
              bitField0_ |= 0x00000001;
              name_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              password_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              version_ = input.readBytes();
              break;
            }
            case 32: {
              int rawValue = input.readEnum();
              com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode value = com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(4, rawValue);
              } else {
                addDistrict(value);
              }
              break;
            }
            case 34: {
              int length = input.readRawVarint32();
              int oldLimit = input.pushLimit(length);
              while(input.getBytesUntilLimit() > 0) {
                int rawValue = input.readEnum();
                com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode value = com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode.valueOf(rawValue);
                if (value == null) {
                  unknownFields.mergeVarintField(4, rawValue);
                } else {
                  addDistrict(value);
                }
              }
              input.popLimit(oldLimit);
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string name = 1;
      private Object name_ = "";
      public boolean hasName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getName() {
        Object ref = name_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setName(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }
      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      void setName(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
      }
      
      // required string password = 2;
      private Object password_ = "";
      public boolean hasPassword() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getPassword() {
        Object ref = password_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          password_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setPassword(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        password_ = value;
        onChanged();
        return this;
      }
      public Builder clearPassword() {
        bitField0_ = (bitField0_ & ~0x00000002);
        password_ = getDefaultInstance().getPassword();
        onChanged();
        return this;
      }
      void setPassword(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        password_ = value;
        onChanged();
      }
      
      // required string version = 3;
      private Object version_ = "";
      public boolean hasVersion() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public String getVersion() {
        Object ref = version_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          version_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setVersion(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        version_ = value;
        onChanged();
        return this;
      }
      public Builder clearVersion() {
        bitField0_ = (bitField0_ & ~0x00000004);
        version_ = getDefaultInstance().getVersion();
        onChanged();
        return this;
      }
      void setVersion(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000004;
        version_ = value;
        onChanged();
      }
      
      // repeated .DistrictCode district = 4;
      private java.util.List<com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode> district_ =
        java.util.Collections.emptyList();
      private void ensureDistrictIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          district_ = new java.util.ArrayList<com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode>(district_);
          bitField0_ |= 0x00000008;
        }
      }
      public java.util.List<com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode> getDistrictList() {
        return java.util.Collections.unmodifiableList(district_);
      }
      public int getDistrictCount() {
        return district_.size();
      }
      public com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode getDistrict(int index) {
        return district_.get(index);
      }
      public Builder setDistrict(
          int index, com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode value) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureDistrictIsMutable();
        district_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addDistrict(com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode value) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureDistrictIsMutable();
        district_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllDistrict(
          Iterable<? extends com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode> values) {
        ensureDistrictIsMutable();
        super.addAll(values, district_);
        onChanged();
        return this;
      }
      public Builder clearDistrict() {
        district_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000008);
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:RequestLoginKey)
    }
    
    static {
      defaultInstance = new RequestLoginKey(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:RequestLoginKey)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_RequestLoginKey_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_RequestLoginKey_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n3core/proto/platform/auth/java/RequestL" +
      "oginKey.proto\032)core/proto/common/java/Di" +
      "strictCode.proto\"c\n\017RequestLoginKey\022\014\n\004n" +
      "ame\030\001 \002(\t\022\020\n\010password\030\002 \002(\t\022\017\n\007version\030\003" +
      " \002(\t\022\037\n\010district\030\004 \003(\0162\r.DistrictCodeBQ\n" +
      "<com.navinfo.opentsp.platform.location.p" +
      "rotocol.platform.authB\021LCRequestLoginKey"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_RequestLoginKey_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_RequestLoginKey_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_RequestLoginKey_descriptor,
              new String[] { "Name", "Password", "Version", "District", },
              RequestLoginKey.class,
              RequestLoginKey.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}