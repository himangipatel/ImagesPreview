# ImagesPreview

### Developed by
[Himangi Patel](https://www.github.com/himangipatel)

### Customized by
[Softpal](https://www.github.com/softpal)

[![](https://jitpack.io/v/softpal/ImagesPreview.svg)](https://jitpack.io/#softpal/ImagesPreview)

**Features**

Easy to Use. <br>
Gallery view <br>
Share and Save functionality

## Installation

Add repository url and dependency in application module gradle file:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  	dependencies {
	      implementation 'com.github.softpal:ImagesPreview:1.3'
	}

## Usage
**1. Start by creating an instance of FilePickUtils and LifeCycleCallBackManager.**

```java
            final ArrayList<PreviewFile> previewFiles = new ArrayList<>();
	     previewFiles.add(new PreviewFile(image path,image description));
	     
            Intent intent = new Intent(AppointmentDetailActivity.this, ImagePreviewActivity.class);
            intent.putExtra(ImagePreviewActivity.IMAGE_LIST, previewFiles);
            intent.putExtra(ImagePreviewActivity.CURRENT_ITEM, position);
            intent.putExtra(ImagePreviewActivity.SHOULD_CACHE, true);
            startActivity(intent);
 ```
 
<p align="center">
  <img src="https://github.com/himangipatel/ImagesPreview/blob/master/device-2018-04-20-125529.png" width="250"/>
  <img src="https://github.com/himangipatel/ImagesPreview/blob/master/device-2018-04-20-125603.png" width="250"/>
</p>
