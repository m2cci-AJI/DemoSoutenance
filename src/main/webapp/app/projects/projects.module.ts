import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { ChargeplanApplicationSharedModule } from 'app/shared';
import {
    ProjectsComponent,
    ProjectsDeleteDialogComponent,
    ProjectsDeletePopupComponent,
    ProjectsDetailComponent,
    projectsPopupRoute,
    projectsRoute,
    ProjectsUpdateComponent
} from './';

const ENTITY_STATES = [...projectsRoute, ...projectsPopupRoute];

@NgModule({
    imports: [ChargeplanApplicationSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProjectsComponent,
        ProjectsDetailComponent,
        ProjectsUpdateComponent,
        ProjectsDeleteDialogComponent,
        ProjectsDeletePopupComponent
    ],
    entryComponents: [ProjectsComponent, ProjectsUpdateComponent, ProjectsDeleteDialogComponent, ProjectsDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChargeplanApplicationProjectsModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
