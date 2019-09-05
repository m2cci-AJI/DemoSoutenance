import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { VisModule3 } from './ng2-vis';
import { ChargeplanApplicationSharedModule } from 'app/shared';
import { Vue3Component, vue3Route } from '.';
import { ReactiveFormsModule } from '@angular/forms';

const ENTITY_STATES = [...vue3Route /*...vue2PopupRoute*/];

@NgModule({
    imports: [ChargeplanApplicationSharedModule, RouterModule.forChild(ENTITY_STATES), VisModule3, ReactiveFormsModule],
    declarations: [Vue3Component],
    entryComponents: [Vue3Component],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChargeplanApplicationVue3Module {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
