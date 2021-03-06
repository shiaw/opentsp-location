// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/java/TerminalRegister.proto

package com.navinfo.opentsp.platform.location.protocol.terminal;

public final class LCTerminalRegister {
  private LCTerminalRegister() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface TerminalRegisterOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional int32 provinceIdentify = 1;
    boolean hasProvinceIdentify();
    int getProvinceIdentify();
    
    // optional int32 cityIdentify = 2;
    boolean hasCityIdentify();
    int getCityIdentify();
    
    // optional string produceCoding = 3;
    boolean hasProduceCoding();
    String getProduceCoding();
    
    // optional string terminalModel = 4;
    boolean hasTerminalModel();
    String getTerminalModel();
    
    // optional string terminalIdentify = 5;
    boolean hasTerminalIdentify();
    String getTerminalIdentify();
    
    // optional int32 licenseColorCode = 6;
    boolean hasLicenseColorCode();
    int getLicenseColorCode();
    
    // optional string license = 7;
    boolean hasLicense();
    String getLicense();
    
    // optional string AuthCoding = 8;
    boolean hasAuthCoding();
    String getAuthCoding();
    
    // optional string sim = 9;
    boolean hasSim();
    String getSim();
  }
  public static final class TerminalRegister extends
      com.google.protobuf.GeneratedMessage
      implements TerminalRegisterOrBuilder {
    // Use TerminalRegister.newBuilder() to construct.
    private TerminalRegister(Builder builder) {
      super(builder);
    }
    private TerminalRegister(boolean noInit) {}
    
    private static final TerminalRegister defaultInstance;
    public static TerminalRegister getDefaultInstance() {
      return defaultInstance;
    }
    
    public TerminalRegister getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCTerminalRegister.internal_static_TerminalRegister_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCTerminalRegister.internal_static_TerminalRegister_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional int32 provinceIdentify = 1;
    public static final int PROVINCEIDENTIFY_FIELD_NUMBER = 1;
    private int provinceIdentify_;
    public boolean hasProvinceIdentify() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getProvinceIdentify() {
      return provinceIdentify_;
    }
    
    // optional int32 cityIdentify = 2;
    public static final int CITYIDENTIFY_FIELD_NUMBER = 2;
    private int cityIdentify_;
    public boolean hasCityIdentify() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getCityIdentify() {
      return cityIdentify_;
    }
    
    // optional string produceCoding = 3;
    public static final int PRODUCECODING_FIELD_NUMBER = 3;
    private Object produceCoding_;
    public boolean hasProduceCoding() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getProduceCoding() {
      Object ref = produceCoding_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          produceCoding_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getProduceCodingBytes() {
      Object ref = produceCoding_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        produceCoding_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string terminalModel = 4;
    public static final int TERMINALMODEL_FIELD_NUMBER = 4;
    private Object terminalModel_;
    public boolean hasTerminalModel() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public String getTerminalModel() {
      Object ref = terminalModel_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          terminalModel_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getTerminalModelBytes() {
      Object ref = terminalModel_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        terminalModel_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string terminalIdentify = 5;
    public static final int TERMINALIDENTIFY_FIELD_NUMBER = 5;
    private Object terminalIdentify_;
    public boolean hasTerminalIdentify() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public String getTerminalIdentify() {
      Object ref = terminalIdentify_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          terminalIdentify_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getTerminalIdentifyBytes() {
      Object ref = terminalIdentify_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        terminalIdentify_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional int32 licenseColorCode = 6;
    public static final int LICENSECOLORCODE_FIELD_NUMBER = 6;
    private int licenseColorCode_;
    public boolean hasLicenseColorCode() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public int getLicenseColorCode() {
      return licenseColorCode_;
    }
    
    // optional string license = 7;
    public static final int LICENSE_FIELD_NUMBER = 7;
    private Object license_;
    public boolean hasLicense() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    public String getLicense() {
      Object ref = license_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          license_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getLicenseBytes() {
      Object ref = license_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        license_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string AuthCoding = 8;
    public static final int AUTHCODING_FIELD_NUMBER = 8;
    private Object authCoding_;
    public boolean hasAuthCoding() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    public String getAuthCoding() {
      Object ref = authCoding_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          authCoding_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getAuthCodingBytes() {
      Object ref = authCoding_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        authCoding_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string sim = 9;
    public static final int SIM_FIELD_NUMBER = 9;
    private Object sim_;
    public boolean hasSim() {
      return ((bitField0_ & 0x00000100) == 0x00000100);
    }
    public String getSim() {
      Object ref = sim_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          sim_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getSimBytes() {
      Object ref = sim_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        sim_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    private void initFields() {
      provinceIdentify_ = 0;
      cityIdentify_ = 0;
      produceCoding_ = "";
      terminalModel_ = "";
      terminalIdentify_ = "";
      licenseColorCode_ = 0;
      license_ = "";
      authCoding_ = "";
      sim_ = "";
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
        output.writeInt32(1, provinceIdentify_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, cityIdentify_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getProduceCodingBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, getTerminalModelBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, getTerminalIdentifyBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeInt32(6, licenseColorCode_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBytes(7, getLicenseBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeBytes(8, getAuthCodingBytes());
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        output.writeBytes(9, getSimBytes());
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
          .computeInt32Size(1, provinceIdentify_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, cityIdentify_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getProduceCodingBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getTerminalModelBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getTerminalIdentifyBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(6, licenseColorCode_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(7, getLicenseBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(8, getAuthCodingBytes());
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(9, getSimBytes());
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
    
    public static TerminalRegister parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static TerminalRegister parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static TerminalRegister parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static TerminalRegister parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static TerminalRegister parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static TerminalRegister parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static TerminalRegister parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static TerminalRegister parseDelimitedFrom(
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
    public static TerminalRegister parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static TerminalRegister parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(TerminalRegister prototype) {
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
       implements TerminalRegisterOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCTerminalRegister.internal_static_TerminalRegister_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCTerminalRegister.internal_static_TerminalRegister_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalRegister.TerminalRegister.newBuilder()
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
        provinceIdentify_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        cityIdentify_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        produceCoding_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        terminalModel_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        terminalIdentify_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        licenseColorCode_ = 0;
        bitField0_ = (bitField0_ & ~0x00000020);
        license_ = "";
        bitField0_ = (bitField0_ & ~0x00000040);
        authCoding_ = "";
        bitField0_ = (bitField0_ & ~0x00000080);
        sim_ = "";
        bitField0_ = (bitField0_ & ~0x00000100);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return TerminalRegister.getDescriptor();
      }
      
      public TerminalRegister getDefaultInstanceForType() {
        return TerminalRegister.getDefaultInstance();
      }
      
      public TerminalRegister build() {
        TerminalRegister result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private TerminalRegister buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        TerminalRegister result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public TerminalRegister buildPartial() {
        TerminalRegister result = new TerminalRegister(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.provinceIdentify_ = provinceIdentify_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.cityIdentify_ = cityIdentify_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.produceCoding_ = produceCoding_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.terminalModel_ = terminalModel_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.terminalIdentify_ = terminalIdentify_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.licenseColorCode_ = licenseColorCode_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.license_ = license_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        result.authCoding_ = authCoding_;
        if (((from_bitField0_ & 0x00000100) == 0x00000100)) {
          to_bitField0_ |= 0x00000100;
        }
        result.sim_ = sim_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof TerminalRegister) {
          return mergeFrom((TerminalRegister)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(TerminalRegister other) {
        if (other == TerminalRegister.getDefaultInstance()) return this;
        if (other.hasProvinceIdentify()) {
          setProvinceIdentify(other.getProvinceIdentify());
        }
        if (other.hasCityIdentify()) {
          setCityIdentify(other.getCityIdentify());
        }
        if (other.hasProduceCoding()) {
          setProduceCoding(other.getProduceCoding());
        }
        if (other.hasTerminalModel()) {
          setTerminalModel(other.getTerminalModel());
        }
        if (other.hasTerminalIdentify()) {
          setTerminalIdentify(other.getTerminalIdentify());
        }
        if (other.hasLicenseColorCode()) {
          setLicenseColorCode(other.getLicenseColorCode());
        }
        if (other.hasLicense()) {
          setLicense(other.getLicense());
        }
        if (other.hasAuthCoding()) {
          setAuthCoding(other.getAuthCoding());
        }
        if (other.hasSim()) {
          setSim(other.getSim());
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
              provinceIdentify_ = input.readInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              cityIdentify_ = input.readInt32();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              produceCoding_ = input.readBytes();
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              terminalModel_ = input.readBytes();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              terminalIdentify_ = input.readBytes();
              break;
            }
            case 48: {
              bitField0_ |= 0x00000020;
              licenseColorCode_ = input.readInt32();
              break;
            }
            case 58: {
              bitField0_ |= 0x00000040;
              license_ = input.readBytes();
              break;
            }
            case 66: {
              bitField0_ |= 0x00000080;
              authCoding_ = input.readBytes();
              break;
            }
            case 74: {
              bitField0_ |= 0x00000100;
              sim_ = input.readBytes();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional int32 provinceIdentify = 1;
      private int provinceIdentify_ ;
      public boolean hasProvinceIdentify() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getProvinceIdentify() {
        return provinceIdentify_;
      }
      public Builder setProvinceIdentify(int value) {
        bitField0_ |= 0x00000001;
        provinceIdentify_ = value;
        onChanged();
        return this;
      }
      public Builder clearProvinceIdentify() {
        bitField0_ = (bitField0_ & ~0x00000001);
        provinceIdentify_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 cityIdentify = 2;
      private int cityIdentify_ ;
      public boolean hasCityIdentify() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getCityIdentify() {
        return cityIdentify_;
      }
      public Builder setCityIdentify(int value) {
        bitField0_ |= 0x00000002;
        cityIdentify_ = value;
        onChanged();
        return this;
      }
      public Builder clearCityIdentify() {
        bitField0_ = (bitField0_ & ~0x00000002);
        cityIdentify_ = 0;
        onChanged();
        return this;
      }
      
      // optional string produceCoding = 3;
      private Object produceCoding_ = "";
      public boolean hasProduceCoding() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public String getProduceCoding() {
        Object ref = produceCoding_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          produceCoding_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setProduceCoding(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        produceCoding_ = value;
        onChanged();
        return this;
      }
      public Builder clearProduceCoding() {
        bitField0_ = (bitField0_ & ~0x00000004);
        produceCoding_ = getDefaultInstance().getProduceCoding();
        onChanged();
        return this;
      }
      void setProduceCoding(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000004;
        produceCoding_ = value;
        onChanged();
      }
      
      // optional string terminalModel = 4;
      private Object terminalModel_ = "";
      public boolean hasTerminalModel() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public String getTerminalModel() {
        Object ref = terminalModel_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          terminalModel_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setTerminalModel(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        terminalModel_ = value;
        onChanged();
        return this;
      }
      public Builder clearTerminalModel() {
        bitField0_ = (bitField0_ & ~0x00000008);
        terminalModel_ = getDefaultInstance().getTerminalModel();
        onChanged();
        return this;
      }
      void setTerminalModel(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000008;
        terminalModel_ = value;
        onChanged();
      }
      
      // optional string terminalIdentify = 5;
      private Object terminalIdentify_ = "";
      public boolean hasTerminalIdentify() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public String getTerminalIdentify() {
        Object ref = terminalIdentify_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          terminalIdentify_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setTerminalIdentify(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        terminalIdentify_ = value;
        onChanged();
        return this;
      }
      public Builder clearTerminalIdentify() {
        bitField0_ = (bitField0_ & ~0x00000010);
        terminalIdentify_ = getDefaultInstance().getTerminalIdentify();
        onChanged();
        return this;
      }
      void setTerminalIdentify(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000010;
        terminalIdentify_ = value;
        onChanged();
      }
      
      // optional int32 licenseColorCode = 6;
      private int licenseColorCode_ ;
      public boolean hasLicenseColorCode() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public int getLicenseColorCode() {
        return licenseColorCode_;
      }
      public Builder setLicenseColorCode(int value) {
        bitField0_ |= 0x00000020;
        licenseColorCode_ = value;
        onChanged();
        return this;
      }
      public Builder clearLicenseColorCode() {
        bitField0_ = (bitField0_ & ~0x00000020);
        licenseColorCode_ = 0;
        onChanged();
        return this;
      }
      
      // optional string license = 7;
      private Object license_ = "";
      public boolean hasLicense() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      public String getLicense() {
        Object ref = license_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          license_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setLicense(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        license_ = value;
        onChanged();
        return this;
      }
      public Builder clearLicense() {
        bitField0_ = (bitField0_ & ~0x00000040);
        license_ = getDefaultInstance().getLicense();
        onChanged();
        return this;
      }
      void setLicense(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000040;
        license_ = value;
        onChanged();
      }
      
      // optional string AuthCoding = 8;
      private Object authCoding_ = "";
      public boolean hasAuthCoding() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      public String getAuthCoding() {
        Object ref = authCoding_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          authCoding_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setAuthCoding(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000080;
        authCoding_ = value;
        onChanged();
        return this;
      }
      public Builder clearAuthCoding() {
        bitField0_ = (bitField0_ & ~0x00000080);
        authCoding_ = getDefaultInstance().getAuthCoding();
        onChanged();
        return this;
      }
      void setAuthCoding(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000080;
        authCoding_ = value;
        onChanged();
      }
      
      // optional string sim = 9;
      private Object sim_ = "";
      public boolean hasSim() {
        return ((bitField0_ & 0x00000100) == 0x00000100);
      }
      public String getSim() {
        Object ref = sim_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          sim_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setSim(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000100;
        sim_ = value;
        onChanged();
        return this;
      }
      public Builder clearSim() {
        bitField0_ = (bitField0_ & ~0x00000100);
        sim_ = getDefaultInstance().getSim();
        onChanged();
        return this;
      }
      void setSim(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000100;
        sim_ = value;
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:TerminalRegister)
    }
    
    static {
      defaultInstance = new TerminalRegister(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TerminalRegister)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TerminalRegister_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TerminalRegister_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n/core/proto/terminal/java/TerminalRegis" +
      "ter.proto\"\326\001\n\020TerminalRegister\022\030\n\020provin" +
      "ceIdentify\030\001 \001(\005\022\024\n\014cityIdentify\030\002 \001(\005\022\025" +
      "\n\rproduceCoding\030\003 \001(\t\022\025\n\rterminalModel\030\004" +
      " \001(\t\022\030\n\020terminalIdentify\030\005 \001(\t\022\030\n\020licens" +
      "eColorCode\030\006 \001(\005\022\017\n\007license\030\007 \001(\t\022\022\n\nAut" +
      "hCoding\030\010 \001(\t\022\013\n\003sim\030\t \001(\tBM\n7com.navinf" +
      "o.opentsp.platform.location.protocol.ter" +
      "minalB\022LCTerminalRegister"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TerminalRegister_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TerminalRegister_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TerminalRegister_descriptor,
              new String[] { "ProvinceIdentify", "CityIdentify", "ProduceCoding", "TerminalModel", "TerminalIdentify", "LicenseColorCode", "License", "AuthCoding", "Sim", },
              TerminalRegister.class,
              TerminalRegister.Builder.class);
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
