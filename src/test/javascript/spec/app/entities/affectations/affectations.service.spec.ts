/* tslint:disable max-line-length */
import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { map, take } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { AffectationsService } from 'app/affectations/affectations.service';
import { Affectations, Colors, IAffectations } from 'app/shared/model/affectations.model';

describe('Service Tests', () => {
    describe('Affectations Service', () => {
        let injector: TestBed;
        let service: AffectationsService;
        let httpMock: HttpTestingController;
        let elemDefault: IAffectations;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(AffectationsService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Affectations(0, currentDate, currentDate, 0, 'AAAAAAA', Colors.rouge);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        dateDebut: currentDate.format(DATE_FORMAT),
                        dateFin: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Affectations', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        dateDebut: currentDate.format(DATE_FORMAT),
                        dateFin: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dateDebut: currentDate,
                        dateFin: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Affectations(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Affectations', async () => {
                const returnedFromService = Object.assign(
                    {
                        dateDebut: currentDate.format(DATE_FORMAT),
                        dateFin: currentDate.format(DATE_FORMAT),
                        charge: 1,
                        commentaire: 'BBBBBB',
                        color: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        dateDebut: currentDate,
                        dateFin: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Affectations', async () => {
                const returnedFromService = Object.assign(
                    {
                        dateDebut: currentDate.format(DATE_FORMAT),
                        dateFin: currentDate.format(DATE_FORMAT),
                        charge: 1,
                        commentaire: 'BBBBBB',
                        color: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dateDebut: currentDate,
                        dateFin: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Affectations', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
