/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChargeplanApplicationTestModule } from '../../../test.module';
import { AffectationsDetailComponent } from 'app/affectations/affectations-detail.component';
import { Affectations } from 'app/shared/model/affectations.model';

describe('Component Tests', () => {
    describe('Affectations Management Detail Component', () => {
        let comp: AffectationsDetailComponent;
        let fixture: ComponentFixture<AffectationsDetailComponent>;
        const route = ({ data: of({ affectations: new Affectations(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ChargeplanApplicationTestModule],
                declarations: [AffectationsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AffectationsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AffectationsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.affectations).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
