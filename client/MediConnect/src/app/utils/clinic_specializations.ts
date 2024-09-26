export interface SpecializationData {
  name: string;
  explanation: string;
  symptoms: { name: string; description: string }[];
  sections: { title: string; description: string }[];
}


export const Specializations = [
  {
    name: 'Dentistry',
    explanation:
      'Our dentistry department offers a complete range of oral health services, focusing on preventive, restorative, and cosmetic dental care. We aim to ensure every patient enjoys optimal dental health and a confident smile through personalized treatments and state-of-the-art technology.',
    symptoms: [
      {
        name: 'Toothache',
        description:
          'Persistent pain in your teeth or gums could indicate decay, infection, or damage that requires immediate dental care.',
      },
      {
        name: 'Bleeding Gums',
        description:
          'Gums that bleed during brushing or flossing may signal the early stages of gum disease or gingivitis.',
      },
      {
        name: 'Bad Breath (Halitosis)',
        description:
          'Chronic bad breath is often caused by poor oral hygiene, gum disease, or other underlying dental conditions that need attention.',
      },
    ],
    sections: [
      {
        title: 'Preventive Care and Maintenance',
        description:
          'We provide routine check-ups, cleanings, and preventive treatments such as sealants and fluoride applications to ensure long-term oral health.',
      },
      {
        title: 'Restorative and Emergency Dental Services',
        description:
          'From fillings and root canals to emergency treatments, we help restore damaged or decayed teeth and relieve dental pain.',
      },
      {
        title: 'Cosmetic Dentistry and Smile Enhancement',
        description:
          'Offering teeth whitening, veneers, and other cosmetic procedures, our team helps you achieve the bright, healthy smile you’ve always wanted.',
      },
    ],
  },
  {
    name: 'General Practice',
    explanation:
      'Our general practice department provides a wide array of healthcare services to address both acute illnesses and chronic conditions. Through personalized care, preventive health strategies, and patient education, we ensure you and your family stay healthy and well.',
    symptoms: [
      {
        name: 'Fatigue or Weakness',
        description:
          'Constant tiredness can be a sign of various conditions, from nutritional deficiencies to more serious health issues like thyroid disease.',
      },
      {
        name: 'Persistent Cough',
        description:
          'A cough that lingers for more than a few weeks could indicate respiratory infections, allergies, or even more serious conditions like asthma.',
      },
      {
        name: 'Frequent Headaches',
        description:
          'Regular headaches, especially if severe or accompanied by other symptoms, may be a sign of underlying health issues that need medical evaluation.',
      },
    ],
    sections: [
      {
        title: 'Comprehensive Primary Care',
        description:
          'Regular health check-ups, screenings, and immunizations to maintain your overall health and prevent disease.',
      },
      {
        title: 'Diagnosis and Treatment of Acute and Chronic Conditions',
        description:
          'Expert diagnosis and care for sudden illnesses, infections, and long-term conditions like diabetes or hypertension.',
      },
      {
        title: 'Patient Education and Lifestyle Support',
        description:
          'Guidance on diet, exercise, mental health, and other lifestyle factors to promote long-term wellness and disease prevention.',
      },
    ],
  },
  {
    name: 'Ophthalmology',
    explanation:
      'Our ophthalmology department specializes in eye health, offering comprehensive care ranging from routine vision exams to the treatment of complex eye conditions. Using the latest medical and surgical techniques, we help maintain and improve your eyesight for a lifetime of healthy vision.',
    symptoms: [
      {
        name: 'Blurred Vision',
        description:
          'Difficulty seeing clearly could indicate refractive errors, cataracts, or other eye issues that require correction.',
      },
      {
        name: 'Eye Pain or Discomfort',
        description:
          'Pain, redness, or sensitivity in the eyes can be symptoms of infections, injuries, or conditions like glaucoma.',
      },
      {
        name: 'Flashes or Floaters',
        description:
          'Sudden flashes of light or an increase in floaters can signal retinal issues and should be checked by an ophthalmologist promptly.',
      },
    ],
    sections: [
      {
        title: 'Routine Eye Exams and Vision Correction',
        description:
          'Offering comprehensive eye exams to evaluate vision and detect any early signs of eye conditions, alongside corrective lens prescriptions.',
      },
      {
        title: 'Diagnosis and Management of Eye Diseases',
        description:
          'Treating a wide range of conditions such as glaucoma, cataracts, and diabetic eye disease with the latest technology and treatments.',
      },
      {
        title: 'Surgical and Non-Surgical Eye Treatments',
        description:
          'Providing advanced surgical care like cataract removal, LASIK, and non-invasive treatments for conditions such as dry eyes and refractive errors.',
      },
    ],
  },
];
