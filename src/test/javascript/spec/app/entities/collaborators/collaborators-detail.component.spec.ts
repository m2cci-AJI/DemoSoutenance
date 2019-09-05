/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChargeplanApplicationTestModule } from '../../../test.module';
import { CollaboratorsDetailComponent } from 'app/collaborators/collaborators-detail.component';
import { Collaborators } from 'app/shared/model/collaborators.model';

describe('Component Tests', () => {
    describe('Collaborators Management Detail Component', () => {
        let comp: CollaboratorsDetailComponent;
        let fixture: ComponentFixture<CollaboratorsDetailComponent>;
        const route = ({ data: of({ collaborators: new Collaborators(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ChargeplanApplicationTestModule],
                declarations: [CollaboratorsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CollaboratorsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CollaboratorsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.collaborators).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
