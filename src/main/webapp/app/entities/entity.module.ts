import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'vue-collaborators',
                loadChildren: './vue-collaborators/vue-3.module#ChargeplanApplicationVue3Module'
            },
            {
                path: 'vue-projects',
                loadChildren: './vue-projects/vue-2.module#ChargeplanApplicationVue2Module'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChargeplanApplicationEntityModule {}
