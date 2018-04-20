# ImagesPreview

### Developed by
[Himangi Patel](https://www.github.com/himangipatel)


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
	      compile 'com.github.himangipatel:ImagesPreview:v0.0.2'
	}

## Usage
**1. Start by creating an instance of FilePickUtils and LifeCycleCallBackManager.**

```java
            Intent intent = new Intent(AppointmentDetailActivity.this, ImagePreviewActivity.class);
            intent.putExtra(ImagePreviewActivity.IMAGE_LIST, pass your list of urls);
            intent.putExtra(ImagePreviewActivity.CURRENT_ITEM, position);
            startActivity(intent);
 ```
 
<p align="center">
  <img src="https://github.com/himangipatel/ImagesPreview/blob/master/device-2018-04-20-125529.png" width="250"/>
  <img src="https://github.com/himangipatel/ImagesPreview/blob/master/device-2018-04-20-125603.png" width="250"/>
</p>
