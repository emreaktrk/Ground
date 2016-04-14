# Ground Plugin

### Callbacks

* OnLocationStartedSeeking()
* OnLocationStoppedSeeking()
* OnGPSFound() returns latlon as String like "29.5123-40.12355"
* OnLocationFound() returns id of found target like "12"
* OnGPSProviderDisabled()
* OnBluetoothDisabled()

### Methods

* start()
* stop()
* setInBackground(boolean value)
* returnData() returns the saved target ids in background  like "12-13"
