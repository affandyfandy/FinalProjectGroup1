import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dobFormat',
  standalone: true
})
export class DobFormatPipe implements PipeTransform {
  transform(value: any): any {
    return new Date(value).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }
}
