import React from 'react';
import { FormField } from 'react-form';
import FileBase64 from 'react-file-base64';

class ImageFileWrapper extends React.Component {
  constructor( props ) {
    super( props );

    this.state = {files: []};
    this.getFiles = this.getFiles.bind(this);
  }

  getFiles(files) {
    this.setState({ files: files })
  }

  render() {

    const { fieldApi, onChange, onBlur, ...rest } = this.props;

    const { setValue } = fieldApi;
    return (
      <FileBase64
        multiple={false}
        onDone={(files) => {
          setValue(files.base64);
        }}
        accept="application/image" {...rest} />
    );
  }
}

const ImageFile = FormField(ImageFileWrapper);

export default ImageFile;