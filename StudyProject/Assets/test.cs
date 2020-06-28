using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

namespace celia.game
{
	public class test : MonoBehaviour
	{
		// Start is called before the first frame update
		void Start()
		{
			
		}

		// Update is called once per frame
		void Update()
		{
            GetComponent<Text>().text = "123";
		}
	}
}