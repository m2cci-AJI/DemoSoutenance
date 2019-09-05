/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChargeplanApplicationTestModule } from '../../../test.module';
import { CollaboratorsDeleteDialogComponent } from 'app/collaborators/collaborators-delete-dialog.component';
import { CollaboratorsService } from 'app/collaborators/collaborators.service';

describe('Component Tests', () => {
    describe('Collaborators Management Delete Component', () => {
        let comp: CollaboratorsDeleteDialogComponent;
        let fixture: ComponentFixture<CollaboratorsDeleteDialogComponent>;
        let service: CollaboratorsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ChargeplanApplicationTestModule],
                declarations: [CollaboratorsDeleteDialogComponent]
            })
                .overrideTemplate(CollaboratorsDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CollaboratorsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CollaboratorsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
