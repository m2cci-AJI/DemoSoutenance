/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChargeplanApplicationTestModule } from '../../../test.module';
import { ProjectsDetailComponent } from 'app/projects/projects-detail.component';
import { Projects } from 'app/shared/model/projects.model';

describe('Component Tests', () => {
    describe('Projects Management Detail Component', () => {
        let comp: ProjectsDetailComponent;
        let fixture: ComponentFixture<ProjectsDetailComponent>;
        const route = ({ data: of({ projects: new Projects(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ChargeplanApplicationTestModule],
                declarations: [ProjectsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ProjectsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProjectsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.projects).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
