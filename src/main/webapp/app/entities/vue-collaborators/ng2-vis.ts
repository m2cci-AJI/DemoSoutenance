import { NgModule } from '@angular/core';

import { VisTimelineDirective, VisTimelineService } from './components/timeline/index';

export * from './components/index';

@NgModule({
    declarations: [VisTimelineDirective],
    exports: [VisTimelineDirective],
    providers: [VisTimelineService]
})
export class VisModule3 {}
