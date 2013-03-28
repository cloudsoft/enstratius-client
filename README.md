enStratius Java API example
===============================

Build
======

    mvn clean install -DENSTRATIUS_API_ACCESS_KEY=your_access_key -DENSTRATIUS_API_SECRET_KEY=your_secret_key

Optional, *-DENSTRATIUS_API_ENDPOINT=https://endpoint*, where default is `ENSTRATIUS_API_ENDPOINT=https://api
.enstratius.com`
Optional, *-DENSTRATIUS_API_VERSION=201x-yy-zz*, where default is `ENSTRATIUS_API_VERSION=2012-06-15`

Running Live Test
=================

    mvn clean install -Plive -DENSTRATIUS_API_ACCESS_KEY=your_access_key 
                             -DENSTRATIUS_API_SECRET_KEY=your_secret_key

License
=======

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
